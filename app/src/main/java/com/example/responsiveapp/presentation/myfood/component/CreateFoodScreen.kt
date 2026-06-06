package com.example.responsiveapp.presentation.myfood.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.responsiveapp.presentation.commoncomponent.TopBar
import com.example.responsiveapp.presentation.myfood.BasicInfoState
import com.example.responsiveapp.presentation.myfood.CreateFoodStep
import com.example.responsiveapp.presentation.myfood.MacronutrientsState
import com.example.responsiveapp.presentation.myfood.MineralsState
import com.example.responsiveapp.presentation.myfood.MyFoodDestination
import com.example.responsiveapp.presentation.myfood.VitaminsState
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun CreateFoodScreen(
    destination: MyFoodDestination,
    currentStep: CreateFoodStep,
    basicInfo: BasicInfoState,
    macros: MacronutrientsState,
    minerals: MineralsState,
    vitamins: VitaminsState,
    canProceedToNutrients: Boolean,
    canSave: Boolean,
    onBack: () -> Unit,
    onNextStep: () -> Unit,
    onFoodNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onServingSizeChanged: (String) -> Unit,
    onServingsPerContainerChanged: (String) -> Unit,
    onCaloriesChanged: (String) -> Unit,
    onProteinChanged: (String) -> Unit,
    onCarbohydratesChanged: (String) -> Unit,
    onTotalFatChanged: (String) -> Unit,
    onFiberChanged: (String) -> Unit,
    onSugarChanged: (String) -> Unit,
    onSodiumChanged: (String) -> Unit,
    onCholesterolChanged: (String) -> Unit,
    onPotassiumChanged: (String) -> Unit,
    onCalciumChanged: (String) -> Unit,
    onIronChanged: (String) -> Unit,
    onVitaminAChanged: (String) -> Unit,
    onVitaminCChanged: (String) -> Unit,
    onVitaminDChanged: (String) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isEditMode = destination is MyFoodDestination.Edit

    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopBar(
            heading = if (isEditMode) "Edit Food" else "Create Food",
            onBack = onBack,
            actions = {
                if (isEditMode) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete food",
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            },
        )

        StepIndicator(
            currentStep = currentStep,
            modifier = Modifier.padding(
                horizontal = MaterialTheme.spacing.md,
                vertical = MaterialTheme.spacing.sm,
            ),
        )

        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                val forward = targetState == CreateFoodStep.NUTRIENTS

                (slideInHorizontally {
                    if (forward) it else -it
                } + fadeIn()) togetherWith
                        (slideOutHorizontally {
                            if (forward) -it else it
                        } + fadeOut())
            },
            label = "foodStep",
            modifier = Modifier.weight(1f),
        ) { step ->
            when (step) {
                CreateFoodStep.BASIC_INFO -> {
                    BasicInfoStep(
                        state = basicInfo,
                        canProceedToNutrients = canProceedToNutrients,
                        onFoodNameChanged = onFoodNameChanged,
                        onDescriptionChanged = onDescriptionChanged,
                        onServingSizeChanged = onServingSizeChanged,
                        onServingsPerContainerChanged = onServingsPerContainerChanged,
                        onNext = onNextStep,
                    )
                }

                CreateFoodStep.NUTRIENTS -> {
                    NutrientsStep(
                        foodName = basicInfo.foodName,
                        servingSize = basicInfo.servingSize,
                        macros = macros,
                        minerals = minerals,
                        vitamins = vitamins,
                        canSave = canSave,
                        isEditMode = isEditMode,
                        onCaloriesChanged = onCaloriesChanged,
                        onProteinChanged = onProteinChanged,
                        onCarbohydratesChanged = onCarbohydratesChanged,
                        onTotalFatChanged = onTotalFatChanged,
                        onFiberChanged = onFiberChanged,
                        onSugarChanged = onSugarChanged,
                        onSodiumChanged = onSodiumChanged,
                        onCholesterolChanged = onCholesterolChanged,
                        onPotassiumChanged = onPotassiumChanged,
                        onCalciumChanged = onCalciumChanged,
                        onIronChanged = onIronChanged,
                        onVitaminAChanged = onVitaminAChanged,
                        onVitaminCChanged = onVitaminCChanged,
                        onVitaminDChanged = onVitaminDChanged,
                        onSave = onSave,
                    )
                }
            }
        }
    }
}