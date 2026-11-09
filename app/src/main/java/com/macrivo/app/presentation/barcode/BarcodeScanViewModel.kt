package com.macrivo.app.presentation.barcode

import androidx.camera.view.LifecycleCameraController
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macrivo.app.core.camera.BarcodeAnalyzer
import com.macrivo.app.core.camera.CameraHelper
import com.macrivo.app.core.utils.Resource
import com.macrivo.app.domain.model.FoodDetail
import com.macrivo.app.domain.model.Serving
import com.macrivo.app.domain.model.foodlog.LogFoodRequest
import com.macrivo.app.domain.use_case.food.GetFoodDetailByBarcodeUseCase
import com.macrivo.app.domain.use_case.food.NO_BARCODE_MATCH_MESSAGE
import com.macrivo.app.domain.use_case.foodlog.LogFoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class BarcodeScanViewModel @Inject constructor(
    private val getFoodDetailByBarcodeUseCase: GetFoodDetailByBarcodeUseCase,
    private val logFoodUseCase: LogFoodUseCase,
    private val cameraHelper: CameraHelper,
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
        analyzer.setEnabled(_state.value.status == BarcodeScanStatus.INITIALIZING)
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

    fun onQuantityIncrement() = changeQuantity(QUANTITY_STEP)

    fun onQuantityDecrement() = changeQuantity(-QUANTITY_STEP)

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
        analyzer.resume()
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

    private fun changeQuantity(delta: Float) {
        updateIn(BarcodeScanStatus.FOOD_DETAILS) {
            it.copy(quantity = (it.quantity + delta).coerceIn(MIN_QUANTITY, MAX_QUANTITY))
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
        const val QUANTITY_STEP = 0.5f
        const val MIN_QUANTITY = 0.5f
        const val MAX_QUANTITY = 99f
        const val ZOOM_EPSILON = 0.05f
        val ZOOM_STEPS = listOf(1f, 2f, 3f, 5f)
        val RESCANNABLE_STATUSES = setOf(
            BarcodeScanStatus.PRODUCT_FOUND,
            BarcodeScanStatus.FOOD_DETAILS,
            BarcodeScanStatus.NOT_FOUND,
            BarcodeScanStatus.ERROR
        )
    }
}
