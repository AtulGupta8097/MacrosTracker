package com.macrivo.app.presentation.barcode

import android.app.Activity
import androidx.camera.view.LifecycleCameraController
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macrivo.app.core.ads.RewardedAdManager
import com.macrivo.app.core.ads.RewardedAdResult
import com.macrivo.app.core.ads.RewardedAdState
import com.macrivo.app.core.ads.ScanUnlockStore
import com.macrivo.app.core.camera.BarcodeAnalyzer
import com.macrivo.app.core.camera.CameraHelper
import com.macrivo.app.core.utils.Resource
import com.macrivo.app.domain.model.FoodDetail
import com.macrivo.app.domain.model.Serving
import com.macrivo.app.domain.model.foodlog.LogFoodRequest
import com.macrivo.app.domain.use_case.food.GetFoodDetailByBarcodeUseCase
import com.macrivo.app.domain.use_case.food.ObserveIsFoodSavedUseCase
import com.macrivo.app.domain.use_case.food.RemoveSavedFoodUseCase
import com.macrivo.app.domain.use_case.food.SaveFoodUseCase
import com.macrivo.app.domain.use_case.food.NO_BARCODE_MATCH_MESSAGE
import com.macrivo.app.domain.use_case.foodlog.LogFoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import javax.inject.Inject

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class BarcodeScanViewModel @Inject constructor(
    private val getFoodDetailByBarcodeUseCase: GetFoodDetailByBarcodeUseCase,
    private val logFoodUseCase: LogFoodUseCase,
    private val cameraHelper: CameraHelper,
    private val rewardedAds: RewardedAdManager, // CHANGED
    private val scanUnlock: ScanUnlockStore, // CHANGED
    private val observeIsFoodSaved: ObserveIsFoodSavedUseCase, // CHANGED
    private val saveFood: SaveFoodUseCase, // CHANGED
    private val removeSavedFood: RemoveSavedFoodUseCase, // CHANGED
) : ViewModel() {

    private val _state = MutableStateFlow(BarcodeScanUiState())
    val state: StateFlow<BarcodeScanUiState> = _state.asStateFlow()

    private val _events = Channel<BarcodeScanEvent>(Channel.BUFFERED)
    val events: Flow<BarcodeScanEvent> = _events.receiveAsFlow()

    private val analyzerExecutor = Executors.newSingleThreadExecutor()
    private val analyzer = BarcodeAnalyzer(::onBarcodeDetected)

    private var lookupJob: Job? = null

    init {
        cameraHelper.zoom
            .map { it.roundedToTenth() }
            .distinctUntilChanged()
            .onEach { zoom ->
                _state.update {
                    it.copy(
                        zoomRatio = zoom.ratio,
                        minZoomRatio = zoom.minRatio,
                        maxZoomRatio = zoom.maxRatio,
                    )
                }
            }
            .launchIn(viewModelScope)

        // CHANGED: bookmark state of the scanned food, for the shared detail screen
        _state
            .map { it.food?.id }
            .distinctUntilChanged()
            .flatMapLatest { id -> if (id == null) flowOf(false) else observeIsFoodSaved(id) }
            .onEach { saved -> _state.update { it.copy(isFoodSaved = saved) } }
            .launchIn(viewModelScope)

        // CHANGED: mirror the unlock credit and the ad state into the UI, and keep the analyzer in step
        combine(scanUnlock.isUnlocked, rewardedAds.state) { unlocked, ad -> unlocked to ad }
            .onEach { (unlocked, ad) ->
                _state.update { it.copy(isUnlocked = unlocked, ad = ad) }
                updateAnalyzer()
                // locked with nothing loaded (e.g. right after the previous scan): get the next ad ready
                if (!unlocked && ad == RewardedAdState.Idle) rewardedAds.load()
            }
            .launchIn(viewModelScope)

        // CHANGED: preload the ad; ask again when the consent flow later allows ads
        if (!scanUnlock.isUnlocked.value) rewardedAds.load()

    }
    private var lastBarcode: String? = null
    private var lastScanAgainAt = 0L

    fun onCameraReady(controller: LifecycleCameraController, lifecycleOwner: LifecycleOwner) {
        _state.update {
            if (it.status == BarcodeScanStatus.SCANNING || it.status == BarcodeScanStatus.CAMERA_UNAVAILABLE) {
                it.copy(status = BarcodeScanStatus.INITIALIZING)
            } else {
                it
            }
        }

        cameraHelper.initialize(controller, lifecycleOwner, ::onCameraError)
        cameraHelper.setCameraLens(_state.value.isFrontCamera)
        cameraHelper.setAnalyzer(analyzerExecutor, analyzer)
        updateAnalyzer() // CHANGED: only analyse frames while a scan is unlocked
    }

    fun onCameraDisposed() {
        analyzer.pause()
        cameraHelper.release()
    }

    fun onCameraError(error: Throwable) {
        analyzer.pause()
        _state.update { it.copy(status = BarcodeScanStatus.CAMERA_UNAVAILABLE) }
    }

    fun onRetryCamera() {
        updateIn(BarcodeScanStatus.CAMERA_UNAVAILABLE) {
            it.copy(status = BarcodeScanStatus.INITIALIZING)
        }
    }

    fun onPreviewStreaming() {
        val capabilities = cameraHelper.getCameraCapabilities()
        val current = _state.value
        val torch = current.isTorchEnabled && capabilities.hasFlash
        val zoom = current.zoomRatio.coerceIn(capabilities.minZoomRatio, capabilities.maxZoomRatio)

        cameraHelper.setTorch(torch)
        cameraHelper.setZoomRatio(zoom)

        _state.update {
            it.copy(
                hasFlash = capabilities.hasFlash,
                isTorchEnabled = torch,
                status = if (it.status == BarcodeScanStatus.INITIALIZING) BarcodeScanStatus.SCANNING else it.status
            )
        }
        updateAnalyzer() // CHANGED
    }

    fun onToggleTorch() {
        val current = _state.value
        if (current.status != BarcodeScanStatus.SCANNING || !current.hasFlash) return
        cameraHelper.setTorch(!current.isTorchEnabled)
        _state.update { it.copy(isTorchEnabled = !it.isTorchEnabled) }
    }

    fun onSwitchCamera() {
        val current = _state.value
        if (current.status != BarcodeScanStatus.SCANNING) return
        cameraHelper.setCameraLens(!current.isFrontCamera)
        _state.update {
            it.copy(
                isFrontCamera = !it.isFrontCamera,
                isTorchEnabled = false,
                hasFlash = false,
                zoomRatio = 1f
            )
        }
    }

    fun onZoomCycle() {
        val current = _state.value
        if (current.status != BarcodeScanStatus.SCANNING) return
        val steps = ZOOM_STEPS
            .filter { it in current.minZoomRatio..current.maxZoomRatio }
            .ifEmpty { return }
        val next = steps.firstOrNull { it > current.zoomRatio + ZOOM_EPSILON } ?: steps.first()
        cameraHelper.setZoomRatio(next)
    }

    fun onBarcodeDetected(rawBarcode: String) {
        if (_state.value.status != BarcodeScanStatus.SCANNING) return

        val barcode = normalizeBarcode(rawBarcode) ?: return
        val isRecentDuplicate = barcode == lastBarcode &&
                System.currentTimeMillis() - lastScanAgainAt < RESCAN_COOLDOWN_MS
        if (isRecentDuplicate) return

        // CHANGED: the one place a scan starts. It needs an earned credit and uses it up atomically, so
        // there is no way to scan without a reward and one reward can never cover two scans.
        if (!scanUnlock.tryConsume()) {
            analyzer.pause()
            return
        }

        analyzer.pause()
        lastBarcode = barcode
        startLookup(barcode)
    }

    fun onRetryLookup() {
        val barcode = _state.value.scannedBarcode ?: return
        if (_state.value.status != BarcodeScanStatus.ERROR) return
        startLookup(barcode)
    }

    fun onViewNutrition() {
        updateIn(BarcodeScanStatus.PRODUCT_FOUND) { it.copy(status = BarcodeScanStatus.FOOD_DETAILS) }
    }

    fun onServingSelected(serving: Serving) {
        updateIn(BarcodeScanStatus.FOOD_DETAILS) {
            if (it.food?.servings?.any { s -> s.id == serving.id } == true) {
                it.copy(selectedServing = serving, quantity = 1f)
            } else {
                it
            }
        }
    }

    // CHANGED: the shared FoodDetailScreen reports the new quantity directly
    fun onQuantityChanged(quantity: Float) {
        updateIn(BarcodeScanStatus.FOOD_DETAILS) {
            it.copy(quantity = quantity.coerceIn(MIN_QUANTITY, MAX_QUANTITY))
        }
    }

    // CHANGED: bookmark button of the shared detail screen
    fun onSaveToggleClicked() {
        val current = _state.value
        val food = current.food ?: return
        viewModelScope.launch {
            if (current.isFoodSaved) removeSavedFood(food.id) else saveFood(food)
        }
    }

    fun onLogFood() {
        val current = _state.value
        if (current.status != BarcodeScanStatus.FOOD_DETAILS) return
        val food = current.food ?: return
        val serving = current.selectedServing ?: return
        val nutrition = current.scaledNutrition ?: return

        _state.update { it.copy(status = BarcodeScanStatus.LOGGING) }

        viewModelScope.launch {
            val request = LogFoodRequest(
                foodName = food.name,
                servingDescription = serving.description,
                quantity = current.quantity,
                nutrition = nutrition,
            )

            when (val result = logFoodUseCase(request)) {
                is Resource.Success -> {
                    _state.update { it.copy(status = BarcodeScanStatus.ADDED) }
                    _events.send(BarcodeScanEvent.Logged)
                }

                is Resource.Error -> {
                    _state.update { it.copy(status = BarcodeScanStatus.FOOD_DETAILS) }
                    _events.send(BarcodeScanEvent.ShowMessage(result.message ?: "Couldn't add to log"))
                }

                is Resource.Loading -> Unit
            }
        }
    }

    fun onScanAnother() {
        val current = _state.value
        if (current.status !in RESCANNABLE_STATUSES) return

        lookupJob?.cancel()
        lastScanAgainAt = System.currentTimeMillis()
        _state.update {
            it.copy(
                status = BarcodeScanStatus.SCANNING,
                scannedBarcode = null,
                food = null,
                selectedServing = null,
                quantity = 1f,
                errorMessage = null
            )
        }
        updateAnalyzer() // CHANGED: stays off until the next scan is unlocked with another ad
    }

    // CHANGED: watch a rewarded ad to earn one scan
    fun onWatchAdClicked(activity: Activity) {
        val current = _state.value
        if (current.isUnlocked || current.status !in SCAN_STAGE_STATUSES) return

        rewardedAds.show(activity) { result ->
            when (result) {
                // the only path that unlocks: the SDK confirmed the reward and the ad was closed
                RewardedAdResult.Earned -> scanUnlock.grant()

                RewardedAdResult.DismissedWithoutReward ->
                    _events.trySend(BarcodeScanEvent.ShowMessage("Watch the full ad to unlock a scan."))

                RewardedAdResult.NotReady ->
                    _events.trySend(BarcodeScanEvent.ShowMessage("The ad isn't ready yet. Please try again in a moment."))

                is RewardedAdResult.FailedToShow ->
                    _events.trySend(BarcodeScanEvent.ShowMessage(result.message))
            }
        }
    }

    fun onRetryAd() = rewardedAds.load()

    // CHANGED: frames are analysed only while the camera is on a scan stage AND a scan is unlocked
    private fun updateAnalyzer() {
        analyzer.setEnabled(scanUnlock.isUnlocked.value && _state.value.status in SCAN_STAGE_STATUSES)
    }

    fun onNavigateBack() {
        when (_state.value.status) {
            BarcodeScanStatus.LOGGING -> Unit
            BarcodeScanStatus.FOOD_DETAILS -> updateIn(BarcodeScanStatus.FOOD_DETAILS) {
                it.copy(status = BarcodeScanStatus.PRODUCT_FOUND)
            }
            else -> viewModelScope.launch { _events.send(BarcodeScanEvent.NavigateBack) }
        }
    }

    private fun startLookup(barcode: String) {
        _state.update {
            it.copy(
                status = BarcodeScanStatus.FETCHING_PRODUCT,
                scannedBarcode = barcode,
                food = null,
                selectedServing = null,
                quantity = 1f,
                errorMessage = null
            )
        }

        lookupJob?.cancel()
        lookupJob = viewModelScope.launch {
            getFoodDetailByBarcodeUseCase(barcode).collect { resource ->
                when (resource) {
                    is Resource.Loading -> Unit
                    is Resource.Success -> onLookupSuccess(resource.data)
                    is Resource.Error -> onLookupError(resource.message)
                }
            }
        }
    }

    private fun onLookupSuccess(food: FoodDetail?) {
        val serving = food?.defaultServing
        _state.update {
            if (food != null && serving != null) {
                it.copy(
                    status = BarcodeScanStatus.PRODUCT_FOUND,
                    food = food,
                    selectedServing = serving,
                    quantity = 1f
                )
            } else {
                it.copy(
                    status = BarcodeScanStatus.ERROR,
                    errorMessage = "No nutrition information is available for this product."
                )
            }
        }
    }

    private fun onLookupError(message: String?) {
        _state.update {
            if (message == NO_BARCODE_MATCH_MESSAGE) {
                it.copy(status = BarcodeScanStatus.NOT_FOUND)
            } else {
                it.copy(
                    status = BarcodeScanStatus.ERROR,
                    errorMessage = message ?: "Something went wrong"
                )
            }
        }
    }

    private fun updateIn(
        vararg statuses: BarcodeScanStatus,
        transform: (BarcodeScanUiState) -> BarcodeScanUiState
    ) {
        _state.update { if (it.status in statuses) transform(it) else it }
    }

    private fun normalizeBarcode(raw: String): String? {
        val digits = raw.filter { it.isDigit() }
        return when (digits.length) {
            8, 12, 13 -> digits.padStart(13, '0')
            14 -> if (digits.startsWith('0')) digits.drop(1) else null
            else -> null
        }
    }

    override fun onCleared() {
        analyzer.close()
        cameraHelper.release()
        analyzerExecutor.shutdown()
        super.onCleared()
    }

    private companion object {
        const val RESCAN_COOLDOWN_MS = 1_500L
        const val MIN_QUANTITY = 0.5f
        const val MAX_QUANTITY = 99f
        const val ZOOM_EPSILON = 0.05f
        val ZOOM_STEPS = listOf(1f, 2f, 3f, 5f)
        val SCAN_STAGE_STATUSES = setOf(BarcodeScanStatus.INITIALIZING, BarcodeScanStatus.SCANNING) // CHANGED
        val RESCANNABLE_STATUSES = setOf(
            BarcodeScanStatus.PRODUCT_FOUND,
            BarcodeScanStatus.FOOD_DETAILS,
            BarcodeScanStatus.NOT_FOUND,
            BarcodeScanStatus.ERROR
        )
    }
}
