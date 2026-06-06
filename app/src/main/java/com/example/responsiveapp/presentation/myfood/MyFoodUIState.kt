package com.example.responsiveapp.presentation.myfood

import androidx.compose.runtime.Immutable
import com.example.responsiveapp.domain.model.CustomToastProperty
import com.example.responsiveapp.domain.model.myfood.CustomFood
import com.example.responsiveapp.presentation.commoncomponent.SuccessToast

enum class CreateFoodStep {
    BASIC_INFO,
    NUTRIENTS
}

@Immutable
data class BasicInfoState(
    val foodName: String = "",
    val description: String = "",
    val servingSize: String = "",
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

    // ViewModel Flags
    val canProceedToNutrients: Boolean = false,
    val canSave: Boolean = false,
    val hasUnsavedChanges: Boolean = false,
    val isSaving: Boolean = false,

    // Toast
    val showToast: Boolean = false,
    val toastType: CustomToastProperty = SuccessToast(),
    val toastMessage: String? = null,
    val toastDuration: Long = 3000
)