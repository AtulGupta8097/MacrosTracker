package com.example.responsiveapp.presentation.mymeal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.responsiveapp.presentation.commoncomponent.CustomToast
import com.example.responsiveapp.presentation.mymeal.component.MyMealPhoneLayout
import com.example.responsiveapp.presentation.mymeal.component.MyMealTabletLayout
import com.example.responsiveapp.presentation.ui.theme.DeviceConfiguration
import com.example.responsiveapp.presentation.ui.theme.deviceConfiguration

@Composable
fun MyMealScreen(
    viewModel: MyMealViewModel = hiltViewModel(),
    onRootChanged: (Boolean) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val totalMacros = state.ingredient.totalMacros()
    val deviceConfiguration = MaterialTheme.deviceConfiguration

    Box(modifier = Modifier.fillMaxSize()) {

        when (deviceConfiguration) {
            DeviceConfiguration.MOBILE -> {
                MyMealPhoneLayout(
                    state = state,
                    totalMacros = totalMacros,
                    onRootChanged = onRootChanged,
                    onCreateMealClick = viewModel::onCreateMealClicked,
                    onMealCardClick = viewModel::onMealCardClick,
                    onMealNameChange = viewModel::onMealNameChange,
                    onShowAddSheet = viewModel::onShowAddSheet,
                    onHideAddSheet = viewModel::onHideAddSheet,
                    onQueryChange = viewModel::onQueryChange,
                    onFoodAdd = viewModel::onAddFoodToMeal,
                    onIngredientRemove = viewModel::onRemoveFoodToMeal,
                    onSave = viewModel::onSaveMeal,
                    onDelete = viewModel::onDeleteMeal,
                    onBack = viewModel::onBack,
                )
            }
            DeviceConfiguration.TABLET,
            DeviceConfiguration.DESKTOP -> {
                MyMealTabletLayout(
                    state = state,
                    totalMacros = totalMacros,
                    onMealCardClick = viewModel::onMealCardClick,
                    onCreateMealClick = viewModel::onCreateMealClicked,
                    onMealNameChange = viewModel::onMealNameChange,
                    onShowAddSheet = viewModel::onShowAddSheet,
                    onHideAddSheet = viewModel::onHideAddSheet,
                    onQueryChange = viewModel::onQueryChange,
                    onFoodAdd = viewModel::onAddFoodToMeal,
                    onIngredientRemove = viewModel::onRemoveFoodToMeal,
                    onSave = viewModel::onSaveMeal,
                    onDelete = viewModel::onDeleteMeal,
                )
            }

        }

        CustomToast(
            message = state.toastMessage ?: "",
            type = state.toastType,
            durationMillis = state.toastDuration,
            onDismiss = viewModel::hideToast,
            visibility = state.showToast
        )
    }
}