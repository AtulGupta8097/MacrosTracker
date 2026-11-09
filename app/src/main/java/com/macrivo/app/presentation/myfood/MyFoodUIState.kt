package com.macrivo.app.presentation.myfood

import androidx.compose.runtime.Immutable
import com.macrivo.app.domain.model.CustomToastProperty
import com.macrivo.app.domain.model.myfood.CustomFood
import com.macrivo.app.presentation.commoncomponent.SuccessToast
import com.macrivo.app.presentation.myfood.validation.BasicInfoErrors
import com.macrivo.app.presentation.myfood.validation.NutrientErrors

enum class CreateFoodStep {
    BASIC_INFO,
    NUTRIENTS
}

@Immutable
data class BasicInfoState(
    val foodName: String = "",
    val description: String = "",
    // CHANGED: servingSize now holds only the amount ("100"); the unit is separate so each
    // field can use the right keyboard. They are joined as "100 g" when saving.
    val servingSize: String = "",
    val servingUnit: String = "",
    val servingsPerContainer: String = ""
)

@Immutable
data class MacronutrientsState(
    val calories: String = "",
    val protein: String = "",
    val carbohydrates: String = "",
    val totalFat: String = "",
    val fiber: String = "",
    val sugar: String = ""
)

@Immutable
data class MineralsState(
    val sodium: String = "",
    val cholesterol: String = "",
    val potassium: String = "",
    val calcium: String = "",
    val iron: String = ""
)

@Immutable
data class VitaminsState(
    val vitaminA: String = "",
    val vitaminC: String = "",
    val vitaminD: String = ""
)

@Immutable
data class MyFoodUIState(
    // Data
    val foods: List<CustomFood> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    // Navigation
    val destination: MyFoodDestination = MyFoodDestination.List,
    val editingFood: CustomFood? = null,

    // Step
    val currentStep: CreateFoodStep = CreateFoodStep.BASIC_INFO,

    val basicInfo: BasicInfoState = BasicInfoState(),
    val macros: MacronutrientsState = MacronutrientsState(),
    val minerals: MineralsState = MineralsState(),
    val vitamins: VitaminsState = VitaminsState(),

    // CHANGED: validation output. Errors only appear after the user has attempted to leave
    // the step / save, then update live while they fix the fields.
    val basicErrors: BasicInfoErrors = BasicInfoErrors(),
    val nutrientErrors: NutrientErrors = NutrientErrors(),
    val basicErrorsVisible: Boolean = false,
    val nutrientErrorsVisible: Boolean = false,
    val calorieWarning: String? = null,

    // ViewModel Flags
    val hasUnsavedChanges: Boolean = false,
    val isSaving: Boolean = false,

    // Toast
    val showToast: Boolean = false,
    val toastType: CustomToastProperty = SuccessToast(),
    val toastMessage: String? = null,
    val toastDuration: Long = 3000
)
