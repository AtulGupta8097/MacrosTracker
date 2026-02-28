package com.example.responsiveapp.presentation.food_browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.domain.repository.FoodLogRepository
import com.example.responsiveapp.domain.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodBrowseViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val foodLogRepository: FoodLogRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FoodBrowseState())
    val state = _state.asStateFlow()
    private var searchJob: Job? = null

    init {
        onQueryChange()
    }

    fun onQueryChange(query: String = "") {
        _state.update { it.copy(query = query) }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400)

            val finalQuery = query.ifBlank { "Pizza" }

            _state.update { it.copy(isLoading = true, error = null) }

            val result = foodRepository.searchFoods(finalQuery, 38)

            result
                .onSuccess { foods ->
                    _state.update {
                        it.copy(
                            foods = foods,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            error = error.message,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun openFoodDetail(foodId: String) {
        _state.update {
            it.copy(
                destination = FoodBrowseDestination.Detail(foodId),
                isDetailLoading = true,
                detailError = null
            )
        }

        viewModelScope.launch {
            val result = foodRepository.getFoodDetail(foodId)

            result
                .onSuccess { food ->
                    _state.update {
                        it.copy(
                            selectedFood = food,
                            isDetailLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            detailError = error.message,
                            isDetailLoading = false
                        )
                    }
                }
        }
    }

    fun onBackFromDetail() {
        _state.update {
            it.copy(
                destination = FoodBrowseDestination.List,
                selectedFood = null
            )
        }
    }
}