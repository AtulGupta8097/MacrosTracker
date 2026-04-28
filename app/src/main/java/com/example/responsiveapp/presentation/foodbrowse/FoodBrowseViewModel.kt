package com.example.responsiveapp.presentation.foodbrowse

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.domain.model.scale
import com.example.responsiveapp.domain.repository.FoodLogRepository
import com.example.responsiveapp.domain.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "FoodBrowseViewModel"

@HiltViewModel
class FoodBrowseViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val foodLogRepository: FoodLogRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(FoodBrowseState())
    val state: StateFlow<FoodBrowseState> = _state.asStateFlow()

    val foodDetailUiState: StateFlow<FoodDetailUiState> = _state
        .map { s ->
            when {
                s.isDetailLoading -> FoodDetailUiState.Loading

                s.detailError != null -> FoodDetailUiState.Error(s.detailError)

                s.selectedFood != null && s.selectedServing != null ->
                    FoodDetailUiState.Success(
                        foodName = s.selectedFood.name,
                        foodBrand = s.selectedFood.brand,
                        servings = s.selectedFood.servings,
                        selectedServing = s.selectedServing,
                        quantity = s.quantity,
                        scaledNutrition = s.selectedServing.nutrition.scale(s.quantity),
                    )

                else -> FoodDetailUiState.Loading
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FoodDetailUiState.Loading,
        )

    init {
        Log.d(TAG, "init")
        onQueryChange()
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared")
        super.onCleared()
    }

    private var searchJob: Job? = null
    private var detailJob: Job? = null

    fun onQueryChange(query: String = "") {
        _state.update { it.copy(query = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400)
            val finalQuery = query.ifBlank { "Pizza" }
            _state.update { it.copy(isLoading = true, error = null) }
            foodRepository.searchFoods(finalQuery, 38)
                .onSuccess { foods ->
                    _state.update { it.copy(foods = foods, isLoading = false, error = null) }
                }
                .onFailure { error ->
                    _state.update { it.copy(error = error.message, isLoading = false) }
                }
        }
    }

    fun openFoodDetail(foodId: String) {
        loadDetail(foodId, manuallySelected = true)
        _state.update { it.copy(destination = FoodBrowseDestination.Detail(foodId)) }
    }

    fun onFirstVisibleFoodChanged(foodId: String) {
        val current = _state.value
        if (current.isManuallySelected) return
        if (current.selectedFood?.id == foodId) return
        loadDetail(foodId, manuallySelected = false)
    }

    fun onServingSelected(serving: Serving) {
        _state.update { it.copy(selectedServing = serving, quantity = 1f) }
    }

    fun onQuantityChanged(quantity: Float) {
        _state.update { it.copy(quantity = quantity) }
    }

    fun onBackFromDetail() {
        _state.update {
            it.copy(
                destination = FoodBrowseDestination.List,
                selectedFood = null,
                selectedServing = null,
                quantity = 1f,
                isDetailLoading = false,
                detailError = null,
                isManuallySelected = false,
            )
        }
    }

    private fun loadDetail(foodId: String, manuallySelected: Boolean) {
        detailJob?.cancel()
        _state.update {
            it.copy(
                isDetailLoading = true,
                detailError = null,
                selectedFood = null,
                selectedServing = null,
                quantity = 1f,
                isManuallySelected = manuallySelected,
            )
        }
        detailJob = viewModelScope.launch {
            foodRepository.getFoodDetail(foodId)
                .onSuccess { food ->
                    _state.update {
                        it.copy(
                            selectedFood = food,
                            selectedServing = food.defaultServing,
                            quantity = 1f,
                            isDetailLoading = false,
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(detailError = error.message, isDetailLoading = false) }
                }
        }
    }
}