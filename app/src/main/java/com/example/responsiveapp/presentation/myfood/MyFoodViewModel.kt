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

                basicInfo = BasicInfoState(
                    foodName = food.name,
                    description = food.description,
                    servingSize = food.servingSize,
                    servingsPerContainer = food.servingsPerContainer.fmt()
                ),

                macros = MacronutrientsState(
                    calories = food.nutrition.calories.fmt(),
                    protein = food.nutrition.protein.fmt(),
                    carbohydrates = food.nutrition.carbs.fmt(),
                    totalFat = food.nutrition.fat.fmt(),
                    fiber = food.nutrition.fiber.fmt(),
                    sugar = food.nutrition.sugar.fmt()
                ),

                minerals = MineralsState(
                    sodium = food.nutrition.sodium.fmt(),
                    cholesterol = food.nutrition.cholesterol.fmt(),
                    potassium = food.nutrition.potassium.fmt(),
                    calcium = food.nutrition.calcium.fmt(),
                    iron = food.nutrition.iron.fmt()
                ),

                vitamins = VitaminsState(
                    vitaminA = food.nutrition.vitaminA.fmt(),
                    vitaminC = food.nutrition.vitaminC.fmt(),
                    vitaminD = food.nutrition.vitaminD.fmt()
                ),

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

    // Basic Info
    fun onFoodNameChanged(v: String) =
        update { it.copy(basicInfo = it.basicInfo.copy(foodName = v)) }

    fun onDescriptionChanged(v: String) =
        update { it.copy(basicInfo = it.basicInfo.copy(description = v)) }

    fun onServingSizeChanged(v: String) =
        update { it.copy(basicInfo = it.basicInfo.copy(servingSize = v)) }

    fun onServingsPerContainerChanged(v: String) =
        update { it.copy(basicInfo = it.basicInfo.copy(servingsPerContainer = v)) }

    // Macros
    fun onCaloriesChanged(v: String) =
        update { it.copy(macros = it.macros.copy(calories = v)) }

    fun onProteinChanged(v: String) =
        update { it.copy(macros = it.macros.copy(protein = v)) }

    fun onCarbohydratesChanged(v: String) =
        update { it.copy(macros = it.macros.copy(carbohydrates = v)) }

    fun onTotalFatChanged(v: String) =
        update { it.copy(macros = it.macros.copy(totalFat = v)) }

    fun onFiberChanged(v: String) =
        update { it.copy(macros = it.macros.copy(fiber = v)) }

    fun onSugarChanged(v: String) =
        update { it.copy(macros = it.macros.copy(sugar = v)) }

    // Minerals
    fun onSodiumChanged(v: String) =
        update { it.copy(minerals = it.minerals.copy(sodium = v)) }

    fun onCholesterolChanged(v: String) =
        update { it.copy(minerals = it.minerals.copy(cholesterol = v)) }

    fun onPotassiumChanged(v: String) =
        update { it.copy(minerals = it.minerals.copy(potassium = v)) }

    fun onCalciumChanged(v: String) =
        update { it.copy(minerals = it.minerals.copy(calcium = v)) }

    fun onIronChanged(v: String) =
        update { it.copy(minerals = it.minerals.copy(iron = v)) }

    // Vitamins
    fun onVitaminAChanged(v: String) =
        update { it.copy(vitamins = it.vitamins.copy(vitaminA = v)) }

    fun onVitaminCChanged(v: String) =
        update { it.copy(vitamins = it.vitamins.copy(vitaminC = v)) }

    fun onVitaminDChanged(v: String) =
        update { it.copy(vitamins = it.vitamins.copy(vitaminD = v)) }

    fun onSave() {
        viewModelScope.launch {
            val s = _state.value

            if (!s.canSave) return@launch

            _state.update {
                it.copy(isSaving = true)
            }

            val food = CustomFood(
                id = s.editingFood?.id ?: UUID.randomUUID().toString(),
                name = s.basicInfo.foodName.trim(),
                description = s.basicInfo.description.trim(),
                servingSize = s.basicInfo.servingSize.trim(),
                servingsPerContainer = s.basicInfo.servingsPerContainer.f(),

                nutrition = NutritionInfo(
                    calories = s.macros.calories.f(),
                    protein = s.macros.protein.f(),
                    carbs = s.macros.carbohydrates.f(),
                    fat = s.macros.totalFat.f(),
                    fiber = s.macros.fiber.f(),
                    sugar = s.macros.sugar.f(),

                    sodium = s.minerals.sodium.f(),
                    cholesterol = s.minerals.cholesterol.f(),
                    potassium = s.minerals.potassium.f(),
                    calcium = s.minerals.calcium.f(),
                    iron = s.minerals.iron.f(),

                    vitaminA = s.vitamins.vitaminA.f(),
                    vitaminC = s.vitamins.vitaminC.f(),
                    vitaminD = s.vitamins.vitaminD.f(),
                ),

                createdAt =
                    s.editingFood?.createdAt ?: System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
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
            basicInfo.foodName.isNotBlank() &&
                    basicInfo.servingSize.isNotBlank()

        val nutrientValid =
            macros.calories.isNotBlank()

        val isEdit =
            editingFood != null

        val changed =
            if (isEdit && originalFood != null) {
                val o = originalFood!!

                basicInfo.foodName.trim() != o.name ||
                        basicInfo.description.trim() != o.description ||
                        basicInfo.servingSize.trim() != o.servingSize ||

                        macros.calories.f() != o.nutrition.calories ||
                        macros.protein.f() != o.nutrition.protein ||
                        macros.carbohydrates.f() != o.nutrition.carbs ||
                        macros.totalFat.f() != o.nutrition.fat ||
                        macros.fiber.f() != o.nutrition.fiber ||
                        macros.sugar.f() != o.nutrition.sugar ||

                        minerals.sodium.f() != o.nutrition.sodium ||
                        minerals.cholesterol.f() != o.nutrition.cholesterol ||
                        minerals.potassium.f() != o.nutrition.potassium ||
                        minerals.calcium.f() != o.nutrition.calcium ||
                        minerals.iron.f() != o.nutrition.iron ||

                        vitamins.vitaminA.f() != o.nutrition.vitaminA ||
                        vitamins.vitaminC.f() != o.nutrition.vitaminC ||
                        vitamins.vitaminD.f() != o.nutrition.vitaminD
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