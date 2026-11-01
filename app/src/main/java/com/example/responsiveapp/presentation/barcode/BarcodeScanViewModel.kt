package com.example.responsiveapp.presentation.barcode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.core.utils.Resource
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.domain.model.foodlog.LogFoodRequest
import com.example.responsiveapp.domain.model.scale
import com.example.responsiveapp.domain.use_case.food.GetFoodDetailByBarcodeUseCase
import com.example.responsiveapp.domain.use_case.food.NO_BARCODE_MATCH_MESSAGE
import com.example.responsiveapp.domain.use_case.foodlog.LogFoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarcodeScanViewModel @Inject constructor(
    private val getFoodDetailByBarcodeUseCase: GetFoodDetailByBarcodeUseCase,
    private val logFoodUseCase: LogFoodUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<BarcodeScanUiState>(BarcodeScanUiState.Scanning)
    val state = _state.asStateFlow()

    private val _events = Channel<String>(Channel.BUFFERED)
    val events: Flow<String> = _events.receiveAsFlow()

    private var lookupJob: Job? = null
    private var lastBarcode: String? = null
    private var lastScanAgainAt = 0L

    fun onBarcodeDetected(rawBarcode: String) {

        if (_state.value !is BarcodeScanUiState.Scanning) return

        val barcode = normalizeBarcode(rawBarcode) ?: return

        if (barcode == lastBarcode &&
            System.currentTimeMillis() - lastScanAgainAt < RESCAN_COOLDOWN_MS
        ) return

        lastBarcode = barcode
        _state.value = BarcodeScanUiState.Loading

        lookupJob?.cancel()
        lookupJob = viewModelScope.launch {
            getFoodDetailByBarcodeUseCase(barcode).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _state.value = BarcodeScanUiState.Loading

                    is Resource.Success -> {
                        val food = resource.data
                        val serving = food?.defaultServing
                        _state.value = if (food == null || serving == null) {
                            BarcodeScanUiState.Error("No nutrition information is available for this product.")
                        } else {
                            BarcodeScanUiState.Found(
                                foodName = food.name,
                                foodBrand = food.brand,
                                servings = food.servings,
                                selectedServing = serving,
                                quantity = 1f,
                                scaledNutrition = serving.nutrition.scale(1f),
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.value = if (resource.message == NO_BARCODE_MATCH_MESSAGE) {
                            BarcodeScanUiState.NotFound
                        } else {
                            BarcodeScanUiState.Error(resource.message ?: "Something went wrong")
                        }
                    }
                }
            }
        }
    }

    fun onServingSelected(serving: Serving) {
        val current = _state.value as? BarcodeScanUiState.Found ?: return
        _state.value = current.copy(
            selectedServing = serving,
            quantity = 1f,
            scaledNutrition = serving.nutrition.scale(1f),
        )
    }

    fun onQuantityChanged(quantity: Float) {
        val current = _state.value as? BarcodeScanUiState.Found ?: return
        val safeQuantity = quantity.coerceAtLeast(0.5f)
        _state.value = current.copy(
            quantity = safeQuantity,
            scaledNutrition = current.selectedServing.nutrition.scale(safeQuantity),
        )
    }

    fun onAddToLog() {
        val current = _state.value as? BarcodeScanUiState.Found ?: return
        if (current.isAdding) return // CHANGED: ignore double taps
        _state.value = current.copy(isAdding = true)

        viewModelScope.launch {
            val request = LogFoodRequest(
                foodName = current.foodName,
                servingDescription = current.selectedServing.description,
                quantity = current.quantity,
                nutrition = current.scaledNutrition,
            )

            when (val result = logFoodUseCase(request)) {
                is Resource.Success -> _state.value = BarcodeScanUiState.Added
                is Resource.Error -> {
                    _state.value = current.copy(isAdding = false)
                    _events.send(result.message ?: "Couldn't add to log")
                }
                is Resource.Loading -> Unit
            }
        }
    }

    fun onScanAgain() {
        val current = _state.value
        if (current is BarcodeScanUiState.Found && current.isAdding) return

        lookupJob?.cancel()
        lastScanAgainAt = System.currentTimeMillis()
        _state.value = BarcodeScanUiState.Scanning
    }

    private companion object {
        const val RESCAN_COOLDOWN_MS = 1_500L
    }
}
