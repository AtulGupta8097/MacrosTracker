package com.example.responsiveapp.presentation.food_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.domain.model.Food
import com.example.responsiveapp.domain.model.FoodLog
import com.example.responsiveapp.domain.model.LogFoodRequest
import com.example.responsiveapp.domain.model.LogMethod
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.domain.use_case.food_usecase.GetFoodByIdUseCase
import com.example.responsiveapp.domain.use_case.foodlog_usecase.LogFoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ─── State ────────────────────────────────────────────────────────────────────

data class FoodDetailsState(
    val food: Food? = null,
    val selectedServing: Serving? = null,
    val quantity: Float = 1f,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val isLoggingFood: Boolean = false,
    val logSuccess: Boolean = false,
    val error: String? = null
)

// ─── Events ───────────────────────────────────────────────────────────────────

sealed class FoodDetailsEvent {
    data class ServingSelected(val serving: Serving) : FoodDetailsEvent()
    data class QuantityChanged(val quantity: Float) : FoodDetailsEvent()
    data class LogFood(val userId: String, val date: Long) : FoodDetailsEvent()
    data object ToggleFavorite : FoodDetailsEvent()
    data object DismissError : FoodDetailsEvent()
    data object DismissSuccess : FoodDetailsEvent()
    data object RetryLoad : FoodDetailsEvent()
}

// ─── ViewModel ────────────────────────────────────────────────────────────────

@HiltViewModel
class FoodDetailsViewModel @Inject constructor(
    private val getFoodByIdUseCase: GetFoodByIdUseCase,
    private val logFoodUseCase: LogFoodUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val foodId: String = checkNotNull(savedStateHandle["foodId"])

    private val _state = MutableStateFlow(FoodDetailsState())
    val state: StateFlow<FoodDetailsState> = _state.asStateFlow()

    init {
        loadFoodDetails()
    }


    private fun loadFoodDetails() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getFoodByIdUseCase(foodId).collectLatest { resource ->
                when (resource) {
                    is com.example.responsiveapp.core.utils.Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is com.example.responsiveapp.core.utils.Resource.Success -> {
                        val food = resource.data
                        val defaultServing = food?.servings?.firstOrNull { it.isDefault }
                            ?: food?.servings?.firstOrNull()
                        _state.update {
                            it.copy(
                                food = food,
                                selectedServing = defaultServing,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is com.example.responsiveapp.core.utils.Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = resource.message ?: "Failed to load food details"
                            )
                        }
                    }
                }
            }
        }
    }

}