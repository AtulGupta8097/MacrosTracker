package com.example.responsiveapp.presentation.myfood

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
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

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        when (MaterialTheme.deviceConfiguration) {

            DeviceConfiguration.MOBILE -> {
                MyFoodPhoneLayout(
                    state = state,
                    onRootChanged = onRootChanged,
                    onCreateFoodClick =
                        viewModel::onCreateFoodClicked,
                    onFoodCardClick =
                        viewModel::onFoodCardClick,
                    onBack =
                        viewModel::onBack,
                    onNextStep =
                        viewModel::onNextStep,
                    onPreviousStep =
                        viewModel::onPreviousStep,

                    onFoodNameChanged =
                        viewModel::onFoodNameChanged,
                    onDescriptionChanged =
                        viewModel::onDescriptionChanged,
                    onServingSizeChanged =
                        viewModel::onServingSizeChanged,
                    onServingsPerContainerChanged =
                        viewModel::onServingsPerContainerChanged,

                    onCaloriesChanged =
                        viewModel::onCaloriesChanged,
                    onProteinChanged =
                        viewModel::onProteinChanged,
                    onCarbohydratesChanged =
                        viewModel::onCarbohydratesChanged,
                    onTotalFatChanged =
                        viewModel::onTotalFatChanged,
                    onFiberChanged =
                        viewModel::onFiberChanged,
                    onSugarChanged =
                        viewModel::onSugarChanged,

                    onSodiumChanged =
                        viewModel::onSodiumChanged,
                    onCholesterolChanged =
                        viewModel::onCholesterolChanged,
                    onPotassiumChanged =
                        viewModel::onPotassiumChanged,
                    onCalciumChanged =
                        viewModel::onCalciumChanged,
                    onIronChanged =
                        viewModel::onIronChanged,

                    onVitaminAChanged =
                        viewModel::onVitaminAChanged,
                    onVitaminCChanged =
                        viewModel::onVitaminCChanged,
                    onVitaminDChanged =
                        viewModel::onVitaminDChanged,

                    onSave =
                        viewModel::onSave,
                    onDelete =
                        viewModel::onDelete,
                )
            }

            DeviceConfiguration.TABLET,
            DeviceConfiguration.DESKTOP -> {

                onRootChanged(true)

                MyFoodTabletLayout(
                    state = state,
                    onCreateFoodClick =
                        viewModel::onCreateFoodClicked,
                    onFoodCardClick =
                        viewModel::onFoodCardClick,
                    onNextStep =
                        viewModel::onNextStep,
                    onPreviousStep =
                        viewModel::onPreviousStep,

                    onFoodNameChanged =
                        viewModel::onFoodNameChanged,
                    onDescriptionChanged =
                        viewModel::onDescriptionChanged,
                    onServingSizeChanged =
                        viewModel::onServingSizeChanged,
                    onServingsPerContainerChanged =
                        viewModel::onServingsPerContainerChanged,

                    onCaloriesChanged =
                        viewModel::onCaloriesChanged,
                    onProteinChanged =
                        viewModel::onProteinChanged,
                    onCarbohydratesChanged =
                        viewModel::onCarbohydratesChanged,
                    onTotalFatChanged =
                        viewModel::onTotalFatChanged,
                    onFiberChanged =
                        viewModel::onFiberChanged,
                    onSugarChanged =
                        viewModel::onSugarChanged,

                    onSodiumChanged =
                        viewModel::onSodiumChanged,
                    onCholesterolChanged =
                        viewModel::onCholesterolChanged,
                    onPotassiumChanged =
                        viewModel::onPotassiumChanged,
                    onCalciumChanged =
                        viewModel::onCalciumChanged,
                    onIronChanged =
                        viewModel::onIronChanged,

                    onVitaminAChanged =
                        viewModel::onVitaminAChanged,
                    onVitaminCChanged =
                        viewModel::onVitaminCChanged,
                    onVitaminDChanged =
                        viewModel::onVitaminDChanged,

                    onSave =
                        viewModel::onSave,
                    onDelete =
                        viewModel::onDelete,
                )
            }
        }

        CustomToast(
            message = state.toastMessage ?: "",
            type = state.toastType,
            durationMillis = state.toastDuration,
            onDismiss = viewModel::hideToast,
            visibility = state.showToast,
        )
    }
}