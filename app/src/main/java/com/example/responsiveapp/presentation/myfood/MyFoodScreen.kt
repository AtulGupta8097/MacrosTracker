package com.example.responsiveapp.presentation.myfood

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.responsiveapp.presentation.commoncomponent.CustomToast
import com.example.responsiveapp.presentation.ui.theme.DeviceConfiguration
import com.example.responsiveapp.presentation.ui.theme.deviceConfiguration

@Composable
fun MyFoodScreen(
    viewModel: MyFoodViewModel = hiltViewModel(),
    onRootChanged: (Boolean) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MyFoodContent(
        state = state,
        onRootChanged = onRootChanged,
        onCreateFoodClick = viewModel::onCreateFoodClicked,
        onFoodCardClick = viewModel::onFoodCardClick,
        onBack = viewModel::onBack,
        onNextStep = viewModel::onNextStep,
        onPreviousStep = viewModel::onPreviousStep,
        onFoodNameChanged = viewModel::onFoodNameChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onServingSizeChanged = viewModel::onServingSizeChanged,
        onServingsPerContainerChanged = viewModel::onServingsPerContainerChanged,
        onCaloriesChanged = viewModel::onCaloriesChanged,
        onProteinChanged = viewModel::onProteinChanged,
        onCarbohydratesChanged = viewModel::onCarbohydratesChanged,
        onTotalFatChanged = viewModel::onTotalFatChanged,
        onFiberChanged = viewModel::onFiberChanged,
        onSugarChanged = viewModel::onSugarChanged,
        onSodiumChanged = viewModel::onSodiumChanged,
        onCholesterolChanged = viewModel::onCholesterolChanged,
        onPotassiumChanged = viewModel::onPotassiumChanged,
        onCalciumChanged = viewModel::onCalciumChanged,
        onIronChanged = viewModel::onIronChanged,
        onVitaminAChanged = viewModel::onVitaminAChanged,
        onVitaminCChanged = viewModel::onVitaminCChanged,
        onVitaminDChanged = viewModel::onVitaminDChanged,
        onSave = viewModel::onSave,
        onDelete = viewModel::onDelete,
        hideToast = viewModel::hideToast
    )
}

@Composable
fun MyFoodContent(
    state: MyFoodUIState,
    onRootChanged: (Boolean) -> Unit,
    onCreateFoodClick: () -> Unit,
    onFoodCardClick: (com.example.responsiveapp.domain.model.myfood.CustomFood) -> Unit,
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
    hideToast: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        when (MaterialTheme.deviceConfiguration) {
            DeviceConfiguration.MOBILE -> {
                MyFoodPhoneLayout(
                    state = state,
                    onRootChanged = onRootChanged,
                    onCreateFoodClick = onCreateFoodClick,
                    onFoodCardClick = onFoodCardClick,
                    onBack = onBack,
                    onNextStep = onNextStep,
                    onPreviousStep = onPreviousStep,
                    onFoodNameChanged = onFoodNameChanged,
                    onDescriptionChanged = onDescriptionChanged,
                    onServingSizeChanged = onServingSizeChanged,
                    onServingsPerContainerChanged = onServingsPerContainerChanged,
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
                    onDelete = onDelete,
                )
            }

            DeviceConfiguration.TABLET,
            DeviceConfiguration.DESKTOP -> {
                onRootChanged(true)
                MyFoodTabletLayout(
                    state = state,
                    onCreateFoodClick = onCreateFoodClick,
                    onFoodCardClick = onFoodCardClick,
                    onNextStep = onNextStep,
                    onPreviousStep = onPreviousStep,
                    onFoodNameChanged = onFoodNameChanged,
                    onDescriptionChanged = onDescriptionChanged,
                    onServingSizeChanged = onServingSizeChanged,
                    onServingsPerContainerChanged = onServingsPerContainerChanged,
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
                    onDelete = onDelete,
                )
            }
        }

        CustomToast(
            message = state.toastMessage ?: "",
            type = state.toastType,
            durationMillis = state.toastDuration,
            onDismiss = hideToast,
            visibility = state.showToast,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PrevMyFoodScreen() {
    MyFoodContent(
        state = MyFoodUIState(),
        onRootChanged = {},
        onCreateFoodClick = {},
        onFoodCardClick = {},
        onBack = {},
        onNextStep = {},
        onPreviousStep = {},
        onFoodNameChanged = {},
        onDescriptionChanged = {},
        onServingSizeChanged = {},
        onServingsPerContainerChanged = {},
        onCaloriesChanged = {},
        onProteinChanged = {},
        onCarbohydratesChanged = {},
        onTotalFatChanged = {},
        onFiberChanged = {},
        onSugarChanged = {},
        onSodiumChanged = {},
        onCholesterolChanged = {},
        onPotassiumChanged = {},
        onCalciumChanged = {},
        onIronChanged = {},
        onVitaminAChanged = {},
        onVitaminCChanged = {},
        onVitaminDChanged = {},
        onSave = {},
        onDelete = {},
        hideToast = {}
    )
}