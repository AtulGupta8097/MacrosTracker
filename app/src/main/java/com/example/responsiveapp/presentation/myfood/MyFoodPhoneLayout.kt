package com.example.responsiveapp.presentation.myfood

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.responsiveapp.domain.model.myfood.CustomFood
import com.example.responsiveapp.presentation.myfood.component.CreateFoodScreen

@Composable
fun MyFoodPhoneLayout(
    modifier: Modifier = Modifier,
    state: MyFoodUIState,
    onRootChanged: (Boolean) -> Unit,
    onCreateFoodClick: () -> Unit,
    onFoodCardClick: (CustomFood) -> Unit,
    onBack: () -> Unit,
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit,
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
) {

    val isOnList =
        state.destination is MyFoodDestination.List

    LaunchedEffect(isOnList) {
        onRootChanged(isOnList)
    }

    BackHandler(
        enabled = !isOnList,
    ) {
        if (state.currentStep == CreateFoodStep.NUTRIENTS) {
            onPreviousStep()
        } else {
            onBack()
        }
    }

    when (state.destination) {

        MyFoodDestination.List -> {
            MyFoodListScreen(
                modifier = modifier,
                foods = state.foods,
                onCreateFoodClick =
                    onCreateFoodClick,
                onFoodCardClick =
                    onFoodCardClick,
            )
        }

        is MyFoodDestination.Create,
        is MyFoodDestination.Edit -> {

            CreateFoodScreen(
                modifier = modifier,
                state = state,

                onBack =
                    if (
                        state.currentStep ==
                        CreateFoodStep.NUTRIENTS
                    ) {
                        onPreviousStep
                    } else {
                        onBack
                    },

                onNextStep =
                    onNextStep,

                onFoodNameChanged =
                    onFoodNameChanged,
                onDescriptionChanged =
                    onDescriptionChanged,
                onServingSizeChanged =
                    onServingSizeChanged,
                onServingsPerContainerChanged =
                    onServingsPerContainerChanged,

                onCaloriesChanged =
                    onCaloriesChanged,
                onProteinChanged =
                    onProteinChanged,
                onCarbohydratesChanged =
                    onCarbohydratesChanged,
                onTotalFatChanged =
                    onTotalFatChanged,
                onFiberChanged =
                    onFiberChanged,
                onSugarChanged =
                    onSugarChanged,

                onSodiumChanged =
                    onSodiumChanged,
                onCholesterolChanged =
                    onCholesterolChanged,
                onPotassiumChanged =
                    onPotassiumChanged,
                onCalciumChanged =
                    onCalciumChanged,
                onIronChanged =
                    onIronChanged,

                onVitaminAChanged =
                    onVitaminAChanged,
                onVitaminCChanged =
                    onVitaminCChanged,
                onVitaminDChanged =
                    onVitaminDChanged,

                onSave =
                    onSave,
                onDelete =
                    onDelete,
            )
        }
    }
}