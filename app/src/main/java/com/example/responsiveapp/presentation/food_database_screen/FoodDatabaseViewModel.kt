package com.example.responsiveapp.presentation.food_database_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.core.utils.Resource
import com.example.responsiveapp.domain.model.Food
import com.example.responsiveapp.domain.use_case.food_usecase.GetFoodByIdUseCase
import com.example.responsiveapp.domain.use_case.food_usecase.SearchFoodsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FoodDatabaseViewModel @Inject constructor(
    private val searchFoodUseCase: SearchFoodsUseCase,
    private val getFoodByIdUseCase: GetFoodByIdUseCase
) : ViewModel() {

    private val _tab1State = MutableStateFlow(FoodDatabaseState<Food>())
    val tab1State = _tab1State.asStateFlow()

    private val _tab2State = MutableStateFlow(FoodDatabaseState<Food>())
    val tab2State = _tab2State.asStateFlow()

    private val _tab3State = MutableStateFlow(FoodDatabaseState<Food>())
    val tab3State = _tab3State.asStateFlow()

    private val _tab4State = MutableStateFlow(FoodDatabaseState<Food>())
    val tab4State = _tab4State.asStateFlow()

    private val _tab1Destination = MutableStateFlow<FoodDatabaseDestination>(FoodDatabaseDestination.FoodList)
    val tab1Destination = _tab1Destination.asStateFlow()

    private val _tab2Destination = MutableStateFlow<MyMealsDestination>(MyMealsDestination.MealList)
    val tab2Destination = _tab2Destination.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun onQueryChange(query: String){
        _searchQuery.value = query
    }

    init {
        observeSearchQuery()
    }
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        searchFoodUseCase("Pizza", 20)
                    } else {
                        searchFoodUseCase(query, 20)
                    }
                }
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _tab1State.value = _tab1State.value.copy(
                                data = result.data ?: emptyList(),
                                isLoading = false,
                                error = null
                            )
                        }

                        is Resource.Error -> {
                            _tab1State.value = _tab1State.value.copy(
                                error = result.message,
                                isLoading = false
                            )
                        }

                        is Resource.Loading -> {
                            _tab1State.value = _tab1State.value.copy(
                                isLoading = true,
                                error = null
                            )
                        }
                    }
                }
        }
    }

    fun getFoodById(foodId: String) {
        viewModelScope.launch {
            getFoodByIdUseCase(foodId).collect { result ->

                when (result) {
                    is Resource.Success -> {
                        _tab1State.value = _tab1State.value.copy(
                            foodDetailData = result.data,
                            foodDetailLoading = false,
                            foodDetailError = null
                        )
                    }

                    is Resource.Error -> {
                        _tab1State.value = _tab1State.value.copy(
                            foodDetailError = result.message,
                            foodDetailLoading = false
                        )
                    }

                    is Resource.Loading -> {
                        _tab1State.value = _tab1State.value.copy(
                            foodDetailLoading = true,
                            foodDetailError = null
                        )
                    }
                }
            }
        }
    }


    fun onTabChanged(tab: FoodTab){
        when(tab){
            FoodTab.ALL -> {}
            FoodTab.MY_MEALS -> {}
            FoodTab.MY_FOODS -> {}
            FoodTab.SAVED_SCANS -> {}
        }
    }

    fun onFoodClick(foodId: String) {
        _tab1Destination.value = FoodDatabaseDestination.FoodDetail(foodId)
        getFoodById(foodId)
    }

    fun onBackFromFoodDetail() {
        _tab1Destination.value = FoodDatabaseDestination.FoodList
    }

    fun onMealClick(mealId: String) {
        _tab2Destination.value = MyMealsDestination.MealDetail(mealId)
    }

    fun onCreateMealClick() {
        _tab2Destination.value = MyMealsDestination.CreateMeal
    }

    fun onBackFromMeals() {
        _tab2Destination.value = MyMealsDestination.MealList
    }
}