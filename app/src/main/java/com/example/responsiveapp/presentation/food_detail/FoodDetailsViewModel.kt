package com.example.responsiveapp.presentation.food_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.domain.model.Food
import com.example.responsiveapp.domain.model.LogFoodRequest
import com.example.responsiveapp.domain.model.LogMethod
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.domain.use_case.food_usecase.GetFoodByIdUseCase
import com.example.responsiveapp.domain.use_case.foodlog_usecase.LogFoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodDetailsViewModel @Inject constructor(
    private val getFoodByIdUseCase: GetFoodByIdUseCase,
    private val logFoodUseCase: LogFoodUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val foodId: String = savedStateHandle.get<String>("foodId") ?: ""
    
    private val _state = MutableStateFlow(FoodDetailsState())
    val state: StateFlow<FoodDetailsState> = _state.asStateFlow()
    
    init {
        loadFoodDetails()
    }
    
    fun onEvent(event: FoodDetailsEvent) {
        when (event) {
            is FoodDetailsEvent.ServingSelected -> {
                _state.update { 
                    it.copy(
                        selectedServing = event.serving,
                        quantity = 1f  // Reset quantity when serving changes
                    ) 
                }
            }
            
            is FoodDetailsEvent.QuantityChanged -> {
                if (event.quantity > 0) {
                    _state.update { it.copy(quantity = event.quantity) }
                }
            }
            
            is FoodDetailsEvent.LogFood -> {
                logFood(event.userId, event.date)
            }
            
            is FoodDetailsEvent.DismissError -> {
                _state.update { it.copy(error = null) }
            }
            
            is FoodDetailsEvent.DismissSuccess -> {
                _state.update { it.copy(logSuccess = false) }
            }
            
            is FoodDetailsEvent.RetryLoad -> {
                loadFoodDetails()
            }
        }
    }
    
    private fun loadFoodDetails() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            getFoodByIdUseCase(foodId)
                .onSuccess { food ->
                    val defaultServing = food.servings.firstOrNull { it.isDefault }
                        ?: food.servings.firstOrNull()
                    
                    _state.update { 
                        it.copy(
                            food = food,
                            selectedServing = defaultServing,
                            isLoading = false,
                            error = null
                        ) 
                    }
                }
                .onFailure { error ->
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load food details"
                        ) 
                    }
                }
        }
    }
    
    private fun logFood(userId: String, date: Long) {
        val currentState = _state.value
        val food = currentState.food ?: return
        val serving = currentState.selectedServing ?: return
        
        viewModelScope.launch {
            _state.update { it.copy(isLoggingFood = true, error = null) }
            
            val request = LogFoodRequest(
                userId = userId,
                foodId = food.id,
                servingId = serving.id,
                quantity = currentState.quantity,
                date = date,
                logMethod = LogMethod.DATABASE,
                notes = null
            )
            
            logFoodUseCase(request)
                .onSuccess {
                    _state.update { 
                        it.copy(
                            isLoggingFood = false,
                            logSuccess = true,
                            error = null
                        ) 
                    }
                }
                .onFailure { error ->
                    _state.update { 
                        it.copy(
                            isLoggingFood = false,
                            error = error.message ?: "Failed to log food"
                        ) 
                    }
                }
        }
    }
}

/**
 * UI State for Food Details
 */
data class FoodDetailsState(
    val food: Food? = null,
    val selectedServing: Serving? = null,
    val quantity: Float = 1f,
    val isLoading: Boolean = false,
    val isLoggingFood: Boolean = false,
    val logSuccess: Boolean = false,
    val error: String? = null
)

/**
 * UI Events for Food Details
 */
sealed class FoodDetailsEvent {
    data class ServingSelected(val serving: Serving) : FoodDetailsEvent()
    data class QuantityChanged(val quantity: Float) : FoodDetailsEvent()
    data class LogFood(val userId: String, val date: Long) : FoodDetailsEvent()
    data object DismissError : FoodDetailsEvent()
    data object DismissSuccess : FoodDetailsEvent()
    data object RetryLoad : FoodDetailsEvent()
}