package com.example.responsiveapp.presentation.food_searach

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.domain.model.Food
import com.example.responsiveapp.domain.use_case.food_usecase.SearchFoodsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodSearchViewModel @Inject constructor(
    private val searchFoodsUseCase: SearchFoodsUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(FoodSearchState())
    val state: StateFlow<FoodSearchState> = _state.asStateFlow()
    
    private var searchJob: Job? = null
    
    fun onEvent(event: FoodSearchEvent) {
        when (event) {
            is FoodSearchEvent.SearchQueryChanged -> {
                _state.update { it.copy(searchQuery = event.query) }
                searchWithDebounce(event.query)
            }
            
            is FoodSearchEvent.FoodSelected -> {
                _state.update { it.copy(selectedFood = event.food) }
            }
            
            is FoodSearchEvent.ClearSearch -> {
                _state.update { 
                    FoodSearchState(searchQuery = it.searchQuery) 
                }
                searchJob?.cancel()
            }
            
            is FoodSearchEvent.RetrySearch -> {
                searchFoods(_state.value.searchQuery)
            }
        }
    }
    
    private fun searchWithDebounce(query: String) {
        searchJob?.cancel()
        
        if (query.length < 2) {
            _state.update { it.copy(searchResults = emptyList(), isLoading = false) }
            return
        }
        
        searchJob = viewModelScope.launch {
            delay(500) // Debounce for 500ms
            searchFoods(query)
        }
    }
    
    private fun searchFoods(query: String) {
        if (query.length < 2) return
        
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            searchFoodsUseCase(query, limit = 20)
                .onSuccess { foods ->
                    _state.update { 
                        it.copy(
                            searchResults = foods,
                            isLoading = false,
                            error = null
                        ) 
                    }
                }
                .onFailure { error ->
                    _state.update { 
                        it.copy(
                            searchResults = emptyList(),
                            isLoading = false,
                            error = error.message ?: "Unknown error occurred"
                        ) 
                    }
                }
        }
    }
}

/**
 * UI State for Food Search
 */
data class FoodSearchState(
    val searchQuery: String = "",
    val searchResults: List<Food> = emptyList(),
    val selectedFood: Food? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * UI Events for Food Search
 */
sealed class FoodSearchEvent {
    data class SearchQueryChanged(val query: String) : FoodSearchEvent()
    data class FoodSelected(val food: Food) : FoodSearchEvent()
    data object ClearSearch : FoodSearchEvent()
    data object RetrySearch : FoodSearchEvent()
}