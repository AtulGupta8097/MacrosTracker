package com.example.responsiveapp.presentation.mymeal

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
import com.example.responsiveapp.presentation.mymeal.component.MyMealPhoneLayout
import com.example.responsiveapp.presentation.mymeal.component.MyMealTabletLayout
import com.example.responsiveapp.presentation.ui.theme.DeviceConfiguration
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.deviceConfiguration

@Composable
fun MyMealScreen(
    viewModel: MyMealViewModel = hiltViewModel(),
    onRootChanged: (Boolean) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MyMealContent(
        state = state,
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
        hideToast = viewModel::hideToast
    )
}

@Composable
fun MyMealContent(
    state: MyMealUIState,
    onRootChanged: (Boolean) -> Unit,
    onCreateMealClick: () -> Unit,
    onMealCardClick: (com.example.responsiveapp.domain.model.mymeals.MyMeal) -> Unit,
    onMealNameChange: (String) -> Unit,
    onShowAddSheet: () -> Unit,
    onHideAddSheet: () -> Unit,
    onQueryChange: (String) -> Unit,
    onFoodAdd: (com.example.responsiveapp.domain.model.FoodItem) -> Unit,
    onIngredientRemove: (String) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
    hideToast: () -> Unit,
) {
    val totalMacros = state.ingredient.totalMacros()
    val deviceConfiguration = MaterialTheme.deviceConfiguration

    Box(modifier = Modifier.fillMaxSize()) {

        when (deviceConfiguration) {
            DeviceConfiguration.MOBILE -> {
                MyMealPhoneLayout(
                    state = state,
                    totalMacros = totalMacros,
                    onRootChanged = onRootChanged,
                    onCreateMealClick = onCreateMealClick,
                    onMealCardClick = onMealCardClick,
                    onMealNameChange = onMealNameChange,
                    onShowAddSheet = onShowAddSheet,
                    onHideAddSheet = onHideAddSheet,
                    onQueryChange = onQueryChange,
                    onFoodAdd = onFoodAdd,
                    onIngredientRemove = onIngredientRemove,
                    onSave = onSave,
                    onDelete = onDelete,
                    onBack = onBack,
                )
            }
            DeviceConfiguration.TABLET,
            DeviceConfiguration.DESKTOP -> {
                MyMealTabletLayout(
                    state = state,
                    totalMacros = totalMacros,
                    onMealCardClick = onMealCardClick,
                    onCreateMealClick = onCreateMealClick,
                    onMealNameChange = onMealNameChange,
                    onShowAddSheet = onShowAddSheet,
                    onHideAddSheet = onHideAddSheet,
                    onQueryChange = onQueryChange,
                    onFoodAdd = onFoodAdd,
                    onIngredientRemove = onIngredientRemove,
                    onSave = onSave,
                    onDelete = onDelete,
                )
            }

        }

        CustomToast(
            message = state.toast.message,
            type = state.toast.type,
            durationMillis = state.toast.duration,
            onDismiss = hideToast,
            visibility = state.toast.visible
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PrevMyMeal() {
    ResponsiveAppTheme {
        MyMealContent(
            state = MyMealUIState(
                destination = MyMealDestination.Create
            ),
            onRootChanged = {},
            onCreateMealClick = {},
            onMealCardClick = {},
            onMealNameChange = {},
            onShowAddSheet = {},
            onHideAddSheet = {},
            onQueryChange = {},
            onFoodAdd = {},
            onIngredientRemove = {},
            onSave = {},
            onDelete = {},
            onBack = {},
            hideToast = {}
        )
    }
}