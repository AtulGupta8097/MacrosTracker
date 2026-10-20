package com.example.responsiveapp.presentation.savedscan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.core.utils.Resource
import com.example.responsiveapp.domain.calculator.ToastState
import com.example.responsiveapp.domain.model.CustomToastProperty
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.domain.model.foodlog.LogFoodRequest
import com.example.responsiveapp.domain.model.scale
import com.example.responsiveapp.domain.use_case.food.ObserveSavedFoodsUseCase
import com.example.responsiveapp.domain.use_case.food.RemoveSavedFoodUseCase
import com.example.responsiveapp.domain.use_case.foodlog.LogFoodUseCase
import com.example.responsiveapp.presentation.commoncomponent.ErrorToast
import com.example.responsiveapp.presentation.commoncomponent.SuccessToast
import com.example.responsiveapp.presentation.foodbrowse.FoodDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// CHANGED: new. Backs the "Saved scans" tab.
@HiltViewModel
class SavedScansViewModel @Inject constructor(
    observeSavedFoods: ObserveSavedFoodsUseCase,
    private val removeSavedFood: RemoveSavedFoodUseCase,
    private val logFood: LogFoodUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SavedScansState())
    val state: StateFlow<SavedScansState> = _state.asStateFlow()

    init {
        observeSavedFoods()
            .onEach { foods -> _state.update { it.copy(savedFoods = foods, isLoading = false) } }
            .launchIn(viewModelScope)
    }

    // Every food here is saved by definition, and the full FoodDetail is
    // already in memory, so there is no loading/error branch and isSaved is
    // always true.
    val foodDetailUiState: StateFlow<FoodDetailUiState> = _state
        .map { s ->
            val food = s.selectedFood
            val serving = s.selectedServing

            if (food != null && serving != null) {
                FoodDetailUiState.Success(
                    foodName = food.name,
                    foodBrand = food.brand,
                    servings = food.servings,
                    selectedServing = serving,
                    quantity = s.quantity,
                    scaledNutrition = serving.nutrition.scale(s.quantity),
                    isSaved = true,
                )
            } else {
                FoodDetailUiState.Loading
            }
        }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FoodDetailUiState.Loading,
        )

    fun onFoodClick(foodId: String) {
        val food = _state.value.savedFoods.firstOrNull { it.id == foodId } ?: return
        _state.update {
            it.copy(
                destination = SavedScansDestination.Detail(foodId),
                selectedFood = food,
                selectedServing = food.defaultServing,
                quantity = 1f,
            )
        }
    }

    fun onBack() {
        _state.update {
            it.copy(
                destination = SavedScansDestination.List,
                selectedFood = null,
                selectedServing = null,
                quantity = 1f,
            )
        }
    }

    fun onServingSelected(serving: Serving) {
        _state.update { it.copy(selectedServing = serving, quantity = 1f) }
    }

    fun onQuantityChanged(quantity: Float) {
        _state.update { it.copy(quantity = quantity) }
    }

    // The bookmark always shows "saved" here, so tapping it can only mean
    // "remove" - take it out of the saved list and go back to the list.
    fun onRemoveFromSaved() {
        val foodId = _state.value.selectedFood?.id ?: return
        viewModelScope.launch {
            removeSavedFood(foodId)
            onBack()
        }
    }

    fun logFoodClicked() {
        val detail = foodDetailUiState.value as? FoodDetailUiState.Success ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLogging = true) }

            try {
                val request = LogFoodRequest(
                    foodName = detail.foodName,
                    servingDescription = detail.selectedServing.description,
                    quantity = detail.quantity,
                    nutrition = detail.scaledNutrition,
                )

                when (val result = logFood(request)) {
                    is Resource.Success -> {
                        showToast(message = "Food logged successfully.", type = SuccessToast())
                        onBack()
                    }

                    is Resource.Error -> {
                        showToast(
                            message = result.message ?: "Unable to log food.",
                            type = ErrorToast(),
                        )
                    }

                    else -> Unit
                }
            } finally {
                _state.update { it.copy(isLogging = false) }
            }
        }
    }

    private fun showToast(
        message: String,
        type: CustomToastProperty = SuccessToast(),
        duration: Long = 3000L,
    ) {
        _state.update {
            it.copy(
                toast = ToastState(
                    visible = true,
                    message = message,
                    type = type,
                    duration = duration,
                )
            )
        }
    }

    fun hideToast() {
        _state.update { it.copy(toast = ToastState()) }
    }
}
