package com.example.responsiveapp.presentation.mymeal.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RamenDining
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.FoodItem
import com.example.responsiveapp.domain.model.mymeals.MyMeal
import com.example.responsiveapp.presentation.mymeal.MyMealDestination
import com.example.responsiveapp.presentation.mymeal.MyMealUIState
import com.example.responsiveapp.presentation.mymeal.TotalMacros

private val LIST_PANE_WIDTH = 300.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMealTabletLayout(
    state             : MyMealUIState,
    totalMacros       : TotalMacros,
    onMealCardClick   : (MyMeal) -> Unit,
    onCreateMealClick : () -> Unit,
    onMealNameChange  : (String) -> Unit,
    onShowAddSheet    : () -> Unit,
    onHideAddSheet    : () -> Unit,
    onQueryChange     : (String) -> Unit,
    onFoodAdd         : (FoodItem) -> Unit,
    onIngredientRemove: (String) -> Unit,
    onSave            : () -> Unit,
    onDelete          : () -> Unit,
) {
    val sheetState   = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val selectedId   = state.editingMeal?.id
    val isEditMode   = state.editingMeal != null
    val isOnList   = state.destination is MyMealDestination.MyMealList


    Row(modifier = Modifier.fillMaxSize()) {

        MyMealListScreen(
            modifier       = Modifier
                .width(LIST_PANE_WIDTH)
                .fillMaxHeight(),
            meals          = state.meals,
            selectedMealId = selectedId,
            onCreateClick  = onCreateMealClick,
            onMealClicked  = onMealCardClick,
        )

        VerticalDivider(
            color     = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp,
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            if (isOnList) {
                RightPaneIdleHint()
            } else {
                CreateMealScreen(
                    modifier = Modifier.fillMaxSize(),
                    mealName = state.mealName,
                    totalCal = totalMacros.calories,
                    totalProtein = totalMacros.protein,
                    totalCarbs = totalMacros.carbs,
                    totalFat = totalMacros.fat,
                    ingredients = state.ingredient,
                    isEditMode = isEditMode,
                    hasUnsavedChanges = state.hasUnsavedChanges,
                    onMealNameChanged = onMealNameChange,
                    onIngredientAdd = onShowAddSheet,
                    onIngredientRemove = onIngredientRemove,
                    onSave = onSave,
                    onDelete = onDelete,
                    onBack = {} // No back button on tablet
                )
            }
        }
    }

    if (state.showCreateSheet) {
        FoodSearchBottomSheet(
            sheetState   = sheetState,
            searchQuery  = state.sheetSearchQuery,
            foods        = state.sheetFoods,
            isLoading    = state.isFoodListLoading,
            errorMessage = state.sheetErrorMessage,
            onQueryChange = onQueryChange,
            onFoodAdd    = onFoodAdd,
            onDismiss    = onHideAddSheet,
        )
    }
}


@Composable
private fun RightPaneIdleHint() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Icon(
                imageVector = Icons.Outlined.RamenDining,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                modifier = Modifier.size(64.dp)
            )

            Text(
                text = "Select a meal to edit\nor tap Create Meal",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = 0.6f
                    ),
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}