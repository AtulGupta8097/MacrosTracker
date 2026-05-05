package com.example.responsiveapp.presentation.mymeal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.core.utils.Resource
import com.example.responsiveapp.domain.model.FoodItem
import com.example.responsiveapp.domain.model.MealIngredient
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.use_case.food_usecase.SearchFoodsUseCase
import com.example.responsiveapp.domain.use_case.foodlog_usecase.LogFoodUseCase
import com.example.responsiveapp.domain.use_case.mymeal.DeleteMyMealUseCase
import com.example.responsiveapp.domain.use_case.mymeal.ObServeMyMealUseCase
import com.example.responsiveapp.domain.use_case.mymeal.SaveMyMealUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyMealViewModel @Inject constructor(
    private val observeMyMeals: ObServeMyMealUseCase,
    private val saveMyMeal: SaveMyMealUseCase,
    private val deleteMyMeal: DeleteMyMealUseCase,
    private val logFood: LogFoodUseCase,
    private val searchFoodUseCase: SearchFoodsUseCase,
): ViewModel() {
    private val _state = MutableStateFlow(MyMealUIState())
    val state = _state.asStateFlow()

    init {
        loadMyMeals()
        observeSearchQuery()
    }

    fun onQueryChange(query: String) {
        _state.update { it.copy(sheetSearchQuery = query) }
    }

    private fun loadMyMeals() {
        observeMyMeals()
            .onEach { meals -> _state.update { it.copy(meals = meals) } }
            .launchIn(viewModelScope)
        searchFood("")
    }

    private fun observeSearchQuery() {
        state
            .map { it.sheetSearchQuery }
            .distinctUntilChanged()
            .onEach { query ->
                if (query.isBlank()) {
                    searchFood(query)
                } else {
                    delay(300)
                    searchFood(query)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun searchFood(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _state.update { it.copy(sheetFoods = emptyList(), isLoading = false) }
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            searchFoodUseCase(query, 38)
                .collect { result ->
                    when (result) {

                        is Resource.Loading -> {
                            _state.update {
                                it.copy(
                                    sheetErrorMessage = null,
                                    sheetFoods = emptyList(),
                                    isFoodListLoading = true
                                )
                            }
                        }

                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    sheetErrorMessage = null,
                                    sheetFoods = result.data ?: emptyList(),
                                    isFoodListLoading = false
                                )
                            }
                        }

                        is Resource.Error -> {
                            _state.update {
                                it.copy(
                                    isFoodListLoading = false,
                                    sheetErrorMessage = result.message
                                )
                            }
                        }
                    }
                }
        }
    }

    fun onAddFoodToMeal(food: FoodItem) {
        viewModelScope.launch {

            val ingredient = MealIngredient(
                foodId = food.id,
                foodName = food.name,
                brand = food.brand,
                quantity = 1f,
                nutrition = NutritionInfo(
                    calories = food.macroSummary.calories,
                    protein = food.macroSummary.protein,
                    fat = food.macroSummary.fat,
                    carbs = food.macroSummary.carbs
                )
            )
            _state.update {
                it.copy(
                    ingredient = it.ingredient.plus(food.id to ingredient)
                )
            }
        }
    }

    fun onRemoveFoodToMeal(foodId: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    ingredient = it.ingredient.minus(foodId)
                )
            }
        }
    }
    fun onBack() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    destination = MyMealDestination.MyMealList
                )
            }
        }
    }

    fun onCreateMealClicked() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    destination = MyMealDestination.Create
                )
            }
        }
    }

}
