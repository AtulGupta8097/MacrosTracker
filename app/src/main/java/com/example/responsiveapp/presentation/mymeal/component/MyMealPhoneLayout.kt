package com.example.responsiveapp.presentation.mymeal.component

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.responsiveapp.domain.model.FoodItem
import com.example.responsiveapp.domain.model.mymeals.MyMeal
import com.example.responsiveapp.presentation.mymeal.MyMealDestination
import com.example.responsiveapp.presentation.mymeal.MyMealUIState
import com.example.responsiveapp.presentation.mymeal.TotalMacros

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMealPhoneLayout(
    modifier: Modifier = Modifier,
    state: MyMealUIState,
    totalMacros: TotalMacros,
    onBack: () -> Unit = {},
    onRootChanged: (Boolean) -> Unit,
    onMealCardClick   : (MyMeal) -> Unit,
    onMealNameChange: (String) -> Unit,
    onCreateMealClick: () -> Unit,
    onShowAddSheet: () -> Unit,
    onHideAddSheet: () -> Unit,
    onQueryChange: (String) -> Unit,
    onFoodAdd: (FoodItem) -> Unit,
    onIngredientRemove: (String) -> Unit,
    onDelete          : () -> Unit,
    onSave: () -> Unit,
) {
    val isOnList = state.destination is MyMealDestination.MyMealList
    val isEditMode = state.destination is MyMealDestination.Edit
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(isOnList) {
        onRootChanged(isOnList)
    }

    BackHandler(enabled = !isOnList) {
        onBack()
    }

    when (state.destination) {
        MyMealDestination.MyMealList -> {
            MyMealListScreen(
                modifier = modifier,
                meals = state.meals,
                onCreateClick = onCreateMealClick,
                onMealClicked = onMealCardClick
            )
        }

        is MyMealDestination.Create,
        is MyMealDestination.Edit -> {
            CreateMealScreen(
                modifier = modifier,
                mealName = state.mealName,
                onMealNameChanged = onMealNameChange,
                ingredients = state.ingredient,
                totalCal = totalMacros.calories,
                totalProtein = totalMacros.protein,
                totalCarbs = totalMacros.carbs,
                totalFat = totalMacros.fat,
                onIngredientAdd = onShowAddSheet,
                onIngredientRemove = onIngredientRemove,
                isEditMode = isEditMode,
                onSave = onSave,
                onDelete = onDelete,
                onBack = onBack,
            )

            if (state.showCreateSheet) {
                FoodSearchBottomSheet(
                    sheetState = sheetState,
                    searchQuery = state.sheetSearchQuery,
                    foods = state.sheetFoods,
                    isLoading = state.isFoodListLoading,
                    errorMessage = state.sheetErrorMessage,
                    onQueryChange = onQueryChange,
                    onFoodAdd = onFoodAdd,
                    onDismiss = onHideAddSheet,
                )
            }
        }

        else -> Unit
    }
}