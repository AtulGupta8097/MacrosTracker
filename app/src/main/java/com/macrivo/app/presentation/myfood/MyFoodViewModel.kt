package com.macrivo.app.presentation.myfood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macrivo.app.domain.model.CustomToastProperty
import com.macrivo.app.domain.model.NutritionInfo
import com.macrivo.app.domain.model.myfood.CustomFood
import com.macrivo.app.domain.use_case.myfood.DeleteCustomFoodUseCase
import com.macrivo.app.domain.use_case.myfood.ObserveCustomFoodsUseCase
import com.macrivo.app.domain.use_case.myfood.SaveCustomFoodUseCase
import com.macrivo.app.presentation.commoncomponent.ErrorToast
import com.macrivo.app.presentation.commoncomponent.SuccessToast
import com.macrivo.app.presentation.myfood.validation.CreateFoodValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.CancellationException
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
        // CHANGED: stored "100 g" -> amount + unit for the two serving inputs.
        val (servingAmount, servingUnit) =
            CreateFoodValidator.splitServing(food.servingSize)

        _state.update {
            it.copy(
                destination = MyFoodDestination.Edit(food.id),
                editingFood = food,
                currentStep = CreateFoodStep.BASIC_INFO,

                basicInfo = BasicInfoState(
                    foodName = food.name,
                    description = food.description,
                    servingSize = servingAmount,
                    servingUnit = servingUnit,
                    servingsPerContainer = food.servingsPerContainer.fmt()
                ),

                macros = MacronutrientsState(
                    calories = CreateFoodValidator.formatNumber(food.nutrition.calories), // CHANGED: calories is required, so 0 must show as "0"
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

    // CHANGED: Next and tapping the "Nutrients" segment share one rule - Basic Info must be
    // valid first, otherwise stay here and surface inline errors.
    fun onNextStep() = goToNutrientsIfBasicValid()

    fun onStepSelected(step: CreateFoodStep) {
        when (step) {
            CreateFoodStep.BASIC_INFO -> onPreviousStep()
            CreateFoodStep.NUTRIENTS ->
                if (_state.value.currentStep != CreateFoodStep.NUTRIENTS) goToNutrientsIfBasicValid()
        }
    }

    // Going back only changes the step; every typed value stays in state.
    fun onPreviousStep() {
        _state.update {
            it.copy(currentStep = CreateFoodStep.BASIC_INFO)
        }
    }

    private fun goToNutrientsIfBasicValid() {
        _state.update { s ->
            val errors = CreateFoodValidator.validateBasicInfo(s.basicInfo)
            if (errors.hasErrors) {
                s.copy(basicErrors = errors, basicErrorsVisible = true)
            } else {
                s.copy(
                    basicErrors = errors,
                    basicErrorsVisible = false,
                    currentStep = CreateFoodStep.NUTRIENTS,
                )
            }
        }
    }

    // Basic Info
    fun onFoodNameChanged(v: String) =
        update { it.copy(basicInfo = it.basicInfo.copy(foodName = v)) }

    fun onDescriptionChanged(v: String) =
        update { it.copy(basicInfo = it.basicInfo.copy(description = v)) }

    fun onServingSizeChanged(v: String) =
        update { it.copy(basicInfo = it.basicInfo.copy(servingSize = v)) }

    fun onServingUnitChanged(v: String) =
        update { it.copy(basicInfo = it.basicInfo.copy(servingUnit = v)) }

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
        // Validate + claim the "saving" flag in one atomic update, so rapid repeated taps
        // can never start two saves.
        var toSave: MyFoodUIState? = null

        _state.update { s ->
            toSave = null
            if (s.isSaving) return@update s

            val basic = CreateFoodValidator.validateBasicInfo(s.basicInfo)
            val nutrients = CreateFoodValidator.validateNutrients(s.macros, s.minerals, s.vitamins)

            if (basic.hasErrors || nutrients.hasErrors) {
                s.copy(
                    basicErrors = basic,
                    nutrientErrors = nutrients,
                    basicErrorsVisible = true,
                    nutrientErrorsVisible = true,
                    currentStep = if (basic.hasErrors) CreateFoodStep.BASIC_INFO else CreateFoodStep.NUTRIENTS,
                )
            } else {
                toSave = s
                s.copy(
                    basicErrors = basic,
                    nutrientErrors = nutrients,
                    isSaving = true,
                )
            }
        }

        val snapshot = toSave
        if (snapshot == null) {
            if (!_state.value.isSaving) {
                showToast("Please fix the highlighted fields", ErrorToast())
            }
            return
        }

        viewModelScope.launch {
            val food = buildFood(snapshot)

            try {
                saveCustomFood(food)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Keep everything the user typed; just unlock the form and tell them.
                _state.update { it.copy(isSaving = false) }
                showToast("Couldn't save the food. Your changes are kept - please try again.", ErrorToast())
                return@launch
            }

            originalFood = null
            // CHANGED: navigate only after a successful save, and keep the toast in the new
            // state (previously the reset wiped it immediately).
            resetToList(
                if (snapshot.editingFood != null) "Food updated!" else "Food saved!"
            )
        }
    }

    private fun buildFood(s: MyFoodUIState): CustomFood {
        fun v(raw: String) = CreateFoodValidator.valueOrZero(raw)
        val now = System.currentTimeMillis()
        return CustomFood(
            id = s.editingFood?.id ?: UUID.randomUUID().toString(),
            name = s.basicInfo.foodName.trim(),
            description = s.basicInfo.description.trim(),
            servingSize = CreateFoodValidator.combineServing(
                s.basicInfo.servingSize, s.basicInfo.servingUnit
            ),
            servingsPerContainer = v(s.basicInfo.servingsPerContainer),
            nutrition = NutritionInfo(
                calories = v(s.macros.calories),
                protein = v(s.macros.protein),
                carbs = v(s.macros.carbohydrates),
                fat = v(s.macros.totalFat),
                fiber = v(s.macros.fiber),
                sugar = v(s.macros.sugar),
                sodium = v(s.minerals.sodium),
                cholesterol = v(s.minerals.cholesterol),
                potassium = v(s.minerals.potassium),
                calcium = v(s.minerals.calcium),
                iron = v(s.minerals.iron),
                vitaminA = v(s.vitamins.vitaminA),
                vitaminC = v(s.vitamins.vitaminC),
                vitaminD = v(s.vitamins.vitaminD),
            ),
            createdAt = s.editingFood?.createdAt ?: now,
            updatedAt = now,
        )
    }

    private fun resetToList(message: String) {
        _state.update { current ->
            MyFoodUIState(
                foods = current.foods,
                destination = MyFoodDestination.List,
                showToast = true,
                toastMessage = message,
                toastType = SuccessToast(),
            )
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            val foodId =
                _state.value.editingFood?.id ?: return@launch

            deleteCustomFood(foodId)

            originalFood = null
            resetToList("Food deleted")
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
        fun v(raw: String) = CreateFoodValidator.valueOrZero(raw)

        val changed =
            if (editingFood != null && originalFood != null) {
                val o = originalFood!!
                val n = o.nutrition

                basicInfo.foodName.trim() != o.name ||
                        basicInfo.description.trim() != o.description ||
                        CreateFoodValidator.combineServing(basicInfo.servingSize, basicInfo.servingUnit) != o.servingSize ||
                        v(basicInfo.servingsPerContainer) != o.servingsPerContainer ||

                        v(macros.calories) != n.calories ||
                        v(macros.protein) != n.protein ||
                        v(macros.carbohydrates) != n.carbs ||
                        v(macros.totalFat) != n.fat ||
                        v(macros.fiber) != n.fiber ||
                        v(macros.sugar) != n.sugar ||

                        v(minerals.sodium) != n.sodium ||
                        v(minerals.cholesterol) != n.cholesterol ||
                        v(minerals.potassium) != n.potassium ||
                        v(minerals.calcium) != n.calcium ||
                        v(minerals.iron) != n.iron ||

                        v(vitamins.vitaminA) != n.vitaminA ||
                        v(vitamins.vitaminC) != n.vitaminC ||
                        v(vitamins.vitaminD) != n.vitaminD
            } else {
                false
            }

        return copy(
            // Once errors have been shown they update live as the user fixes the fields.
            basicErrors = if (basicErrorsVisible)
                CreateFoodValidator.validateBasicInfo(basicInfo) else basicErrors,
            nutrientErrors = if (nutrientErrorsVisible)
                CreateFoodValidator.validateNutrients(macros, minerals, vitamins) else nutrientErrors,
            calorieWarning = CreateFoodValidator.calorieWarning(macros),
            hasUnsavedChanges = changed,
        )
    }

    // Float -> text for the edit form. 0 shows as an empty (optional) field.
    private fun Float.fmt() =
        if (this == 0f) "" else CreateFoodValidator.formatNumber(this)
}
