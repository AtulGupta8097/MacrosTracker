package com.example.responsiveapp.presentation.myfood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.domain.model.CustomToastProperty
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.model.myfood.CustomFood
import com.example.responsiveapp.domain.use_case.myfood.DeleteCustomFoodUseCase
import com.example.responsiveapp.domain.use_case.myfood.ObserveCustomFoodsUseCase
import com.example.responsiveapp.domain.use_case.myfood.SaveCustomFoodUseCase
import com.example.responsiveapp.presentation.commoncomponent.SuccessToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MyFoodViewModel @Inject constructor(
    observeCustomFoods: ObserveCustomFoodsUseCase,
    private val saveCustomFood: SaveCustomFoodUseCase,
    private val deleteCustomFood: DeleteCustomFoodUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MyFoodUIState())
    val state = _state.asStateFlow()

    private var originalFood: CustomFood? = null

    init {
        observeCustomFoods()
            .onEach { foods ->
                _state.update {
                    it.copy(foods = foods)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onCreateFoodClicked() {
        originalFood = null

        _state.update { current ->
            MyFoodUIState(
                foods = current.foods,
                destination = MyFoodDestination.Create
            )
        }
    }

    fun onFoodCardClick(food: CustomFood) {
        originalFood = food

        _state.update {
            it.copy(
                destination = MyFoodDestination.Edit(food.id),
                editingFood = food,
                currentStep = CreateFoodStep.BASIC_INFO,

                foodName = food.name,
                description = food.description,
                servingSize = food.servingSize,
                servingsPerContainer = food.servingsPerContainer.fmt(),

                calories = food.nutrition.calories.fmt(),
                protein = food.nutrition.protein.fmt(),
                carbohydrates = food.nutrition.carbs.fmt(),
                totalFat = food.nutrition.fat.fmt(),
                fiber = food.nutrition.fiber.fmt(),
                sugar = food.nutrition.sugar.fmt(),

                sodium = food.nutrition.sodium.fmt(),
                cholesterol = food.nutrition.cholesterol.fmt(),
                potassium = food.nutrition.potassium.fmt(),
                calcium = food.nutrition.calcium.fmt(),
                iron = food.nutrition.iron.fmt(),

                vitaminA = food.nutrition.vitaminA.fmt(),
                vitaminC = food.nutrition.vitaminC.fmt(),
                vitaminD = food.nutrition.vitaminD.fmt(),

                hasUnsavedChanges = false,
            ).recompute()
        }
    }

    fun onBack() {
        originalFood = null

        _state.update { current ->
            MyFoodUIState(
                foods = current.foods,
                destination = MyFoodDestination.List
            )
        }
    }

    fun onNextStep() {
        if (_state.value.canProceedToNutrients) {
            _state.update {
                it.copy(currentStep = CreateFoodStep.NUTRIENTS)
            }
        }
    }

    fun onPreviousStep() {
        _state.update {
            it.copy(currentStep = CreateFoodStep.BASIC_INFO)
        }
    }

    fun onFoodNameChanged(v: String) =
        update { it.copy(foodName = v) }

    fun onDescriptionChanged(v: String) =
        update { it.copy(description = v) }

    fun onServingSizeChanged(v: String) =
        update { it.copy(servingSize = v) }

    fun onServingsPerContainerChanged(v: String) =
        update { it.copy(servingsPerContainer = v) }


    fun onCaloriesChanged(v: String) =
        update { it.copy(calories = v) }

    fun onProteinChanged(v: String) =
        update { it.copy(protein = v) }

    fun onCarbohydratesChanged(v: String) =
        update { it.copy(carbohydrates = v) }

    fun onTotalFatChanged(v: String) =
        update { it.copy(totalFat = v) }

    fun onFiberChanged(v: String) =
        update { it.copy(fiber = v) }

    fun onSugarChanged(v: String) =
        update { it.copy(sugar = v) }

    // ── Minerals ───────────────────────────────────────────

    fun onSodiumChanged(v: String) =
        update { it.copy(sodium = v) }

    fun onCholesterolChanged(v: String) =
        update { it.copy(cholesterol = v) }

    fun onPotassiumChanged(v: String) =
        update { it.copy(potassium = v) }

    fun onCalciumChanged(v: String) =
        update { it.copy(calcium = v) }

    fun onIronChanged(v: String) =
        update { it.copy(iron = v) }

    fun onVitaminAChanged(v: String) =
        update { it.copy(vitaminA = v) }

    fun onVitaminCChanged(v: String) =
        update { it.copy(vitaminC = v) }

    fun onVitaminDChanged(v: String) =
        update { it.copy(vitaminD = v) }

    fun onSave() {
        viewModelScope.launch {
            val s = _state.value

            if (!s.canSave) return@launch

            _state.update {
                it.copy(isSaving = true)
            }

            val food = CustomFood(
                id = s.editingFood?.id ?: UUID.randomUUID().toString(),
                name = s.foodName.trim(),
                description = s.description.trim(),
                servingSize = s.servingSize.trim(),
                servingsPerContainer = s.servingsPerContainer.f(),

                nutrition = NutritionInfo(
                    calories = s.calories.f(),
                    protein = s.protein.f(),
                    carbs = s.carbohydrates.f(),
                    fat = s.totalFat.f(),
                    fiber = s.fiber.f(),
                    sugar = s.sugar.f(),

                    sodium = s.sodium.f(),
                    cholesterol = s.cholesterol.f(),
                    potassium = s.potassium.f(),
                    calcium = s.calcium.f(),
                    iron = s.iron.f(),

                    vitaminA = s.vitaminA.f(),
                    vitaminC = s.vitaminC.f(),
                    vitaminD = s.vitaminD.f(),
                ),

                createdAt =
                    s.editingFood?.createdAt ?: System.currentTimeMillis(),
            )

            saveCustomFood(food)

            showToast(
                if (s.editingFood != null)
                    "Food updated!"
                else
                    "Food saved!",
                SuccessToast()
            )

            originalFood = null

            _state.update { current ->
                MyFoodUIState(
                    foods = current.foods,
                    destination = MyFoodDestination.List
                )
            }
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            val foodId =
                _state.value.editingFood?.id ?: return@launch

            deleteCustomFood(foodId)

            showToast(
                "Food deleted",
                SuccessToast()
            )

            originalFood = null

            _state.update { current ->
                MyFoodUIState(
                    foods = current.foods,
                    destination = MyFoodDestination.List
                )
            }
        }
    }

    private fun showToast(
        message: String,
        type: CustomToastProperty = SuccessToast(),
        duration: Long = 3000,
    ) {
        _state.update {
            it.copy(showToast = false)
        }

        _state.update {
            it.copy(
                showToast = true,
                toastMessage = message,
                toastType = type,
                toastDuration = duration,
                isSaving = false,
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

    private fun update(
        block: (MyFoodUIState) -> MyFoodUIState
    ) {
        _state.update {
            block(it).recompute()
        }
    }

    private fun MyFoodUIState.recompute(): MyFoodUIState {
        val basicValid =
            foodName.isNotBlank() &&
                    servingSize.isNotBlank()

        val nutrientValid =
            calories.isNotBlank()

        val isEdit =
            editingFood != null

        val changed =
            if (isEdit && originalFood != null) {
                val o = originalFood!!

                foodName.trim() != o.name ||
                        description.trim() != o.description ||
                        servingSize.trim() != o.servingSize ||

                        calories.f() != o.nutrition.calories ||
                        protein.f() != o.nutrition.protein ||
                        carbohydrates.f() != o.nutrition.carbs ||
                        totalFat.f() != o.nutrition.fat ||
                        fiber.f() != o.nutrition.fiber ||
                        sugar.f() != o.nutrition.sugar ||

                        sodium.f() != o.nutrition.sodium ||
                        cholesterol.f() != o.nutrition.cholesterol ||
                        potassium.f() != o.nutrition.potassium ||
                        calcium.f() != o.nutrition.calcium ||
                        iron.f() != o.nutrition.iron ||

                        vitaminA.f() != o.nutrition.vitaminA ||
                        vitaminC.f() != o.nutrition.vitaminC ||
                        vitaminD.f() != o.nutrition.vitaminD
            } else {
                false
            }

        return copy(
            canProceedToNutrients = basicValid,
            canSave =
                basicValid &&
                        nutrientValid &&
                        (!isEdit || changed),
            hasUnsavedChanges = changed,
        )
    }

    private fun String.f() =
        toFloatOrNull() ?: 0f

    private fun Float.fmt() =
        if (this == 0f) {
            ""
        } else if (this == toLong().toFloat()) {
            toLong().toString()
        } else {
            toString()
        }
}