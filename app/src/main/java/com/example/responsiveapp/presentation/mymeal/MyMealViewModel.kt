package com.example.responsiveapp.presentation.mymeal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.core.utils.Resource
import com.example.responsiveapp.domain.model.CustomToastProperty
import com.example.responsiveapp.domain.model.FoodItem
import com.example.responsiveapp.domain.model.MealIngredient
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.model.mymeals.MyMeal
import com.example.responsiveapp.domain.use_case.food_usecase.SearchFoodsUseCase
import com.example.responsiveapp.domain.use_case.mymeal.DeleteMyMealUseCase
import com.example.responsiveapp.domain.use_case.mymeal.ObServeMyMealUseCase
import com.example.responsiveapp.domain.use_case.mymeal.SaveMyMealUseCase
import com.example.responsiveapp.presentation.commoncomponent.ErrorToast
import com.example.responsiveapp.presentation.commoncomponent.SuccessToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MyMealViewModel @Inject constructor(
    private val observeMyMeals: ObServeMyMealUseCase,
    private val saveMyMeal: SaveMyMealUseCase,
    private val deleteMyMeal: DeleteMyMealUseCase,
    private val searchFoodUseCase: SearchFoodsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MyMealUIState())
    val state = _state.asStateFlow()

    private var originalMealName: String = ""
    private var originalIngredients: Map<String, MealIngredient> = emptyMap()
    private var searchJob: Job? = null

    init {
        loadMyMeals()
    }

    fun onQueryChange(query: String) {
        _state.update {
            it.copy(sheetSearchQuery = query)
        }

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            if (query.isNotBlank()) {
                delay(300)
            }

            searchFood(query)
        }
    }

    fun onMealNameChange(mealName: String) {
        _state.update {
            it.copy(mealName = mealName)
        }
        recomputeUnsavedChanges()
    }

    private fun loadMyMeals() {
        observeMyMeals()
            .onEach { meals ->
                _state.update {
                    it.copy(meals = meals)
                }
            }
            .launchIn(viewModelScope)

    }

    fun searchFood(query: String) {
        viewModelScope.launch {

            _state.update {
                it.copy(isLoading = true)
            }

            searchFoodUseCase(query, 38)
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _state.update {
                                it.copy(
                                    sheetErrorMessage = null,
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

    private fun recomputeUnsavedChanges() {
        val currentState = _state.value
        val isEdit = currentState.editingMeal != null
        val changed = isEdit && (
                currentState.mealName.trim() != originalMealName.trim() ||
                        currentState.ingredient.keys != originalIngredients.keys
                )
        if (currentState.hasUnsavedChanges != changed) {
            _state.update { it.copy(hasUnsavedChanges = changed) }
        }
    }

    fun onMealCardClick(meal: MyMeal) {
        val ingredientMap = meal.ingredients.associateBy { it.foodId }
        originalMealName  = meal.name
        originalIngredients = ingredientMap

        _state.update {
            it.copy(
                destination = MyMealDestination.Edit(meal.id),
                editingMeal = meal,
                mealName = meal.name,
                ingredient = ingredientMap,
                hasUnsavedChanges = false,
            )
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
                    showCreateSheet = false,
                    ingredient = it.ingredient.plus(food.id to ingredient)
                )
            }
            recomputeUnsavedChanges()
        }
    }

    fun onRemoveFoodToMeal(foodId: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    ingredient = it.ingredient.minus(foodId)
                )
            }
            recomputeUnsavedChanges()

        }
    }

    fun onDeleteMeal() {
        viewModelScope.launch {
            val mealId = _state.value.editingMeal?.id ?: return@launch
            deleteMyMeal(mealId)
            showToast("Meal deleted", SuccessToast())
            _state.update {
                it.copy(
                    destination = MyMealDestination.MyMealList,
                    editingMeal = null,
                    mealName    = "",
                    ingredient  = emptyMap(),
                )
            }
        }
    }

    fun onBack() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    destination = MyMealDestination.MyMealList,
                    editingMeal = null,
                    mealName    = "",
                    ingredient  = emptyMap(),
                    hasUnsavedChanges = false,
                )
            }
        }
    }

    fun onCreateMealClicked() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    destination = MyMealDestination.Create,
                    editingMeal = null,
                    mealName = "",
                    ingredient = emptyMap(),
                )
            }
        }
    }

    fun onShowAddSheet() {
        _state.update {
            it.copy(
                showCreateSheet = true,
                sheetSearchQuery = it.sheetSearchQuery
            )
        }

        searchFood("")
    }

    fun onHideAddSheet() {
        _state.update {
            it.copy(
                showCreateSheet = false,
                sheetSearchQuery = ""
            )
        }
    }

    fun onSaveMeal() {
        viewModelScope.launch {
            val current = _state.value

            if (current.mealName.isBlank()) {
                showToast(
                    message = "Please enter meal name",
                    type = ErrorToast()
                )
                return@launch
            }

            else if(current.ingredient.isEmpty()) {
                showToast(
                    message = "Please add at least one food",
                    type = ErrorToast()
                )
                return@launch
            }

            val totalNutrition =
                current.ingredient.values.fold(NutritionInfo()) { acc, ingredient ->
                    acc.copy(
                        calories = acc.calories + ingredient.nutrition.calories,
                        protein = acc.protein + ingredient.nutrition.protein,
                        carbs = acc.carbs + ingredient.nutrition.carbs,
                        fat = acc.fat + ingredient.nutrition.fat,
                        fiber = acc.fiber + ingredient.nutrition.fiber,
                        sugar = acc.sugar + ingredient.nutrition.sugar,
                        sodium = acc.sodium + ingredient.nutrition.sodium,
                    )
                }


            val mealId = current.editingMeal?.id ?: UUID.randomUUID().toString()

            val myMeal = MyMeal(
                id = mealId,
                name = current.mealName,
                ingredients = current.ingredient.values.toList(),
                totalNutritionInfo = totalNutrition,
                createAt = System.currentTimeMillis()
            )

            saveMyMeal(myMeal)
            val successMsg = if(current.editingMeal != null) "Meal updated successfully" else "Meal saved successfully"

            showToast(
                message = successMsg,
                type = SuccessToast()
            )

            _state.update {
                it.copy(
                    mealName = "",
                    ingredient = emptyMap(),
                    destination = MyMealDestination.MyMealList,
                    editingMeal = null,
                    hasUnsavedChanges = false,
                )
            }
        }
    }

    private fun showToast(
        message: String,
        type: CustomToastProperty = SuccessToast(),
        duration: Long = 3000
    ) {
        _state.update {
            it.copy(showToast = false)
        }

        _state.update {
            it.copy(
                showToast = true,
                toastMessage = message,
                toastType = type,
                toastDuration = duration
            )
        }
    }

    fun hideToast() {
        _state.update {
            it.copy(
                showToast = false,
                toastMessage = null
            )
        }
    }
}