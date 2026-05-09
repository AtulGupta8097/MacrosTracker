package com.example.responsiveapp.presentation.mymeal.component

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.responsiveapp.presentation.mymeal.MyMealDestination
import com.example.responsiveapp.presentation.mymeal.MyMealUIState
import com.example.responsiveapp.presentation.mymeal.TotalMacros

@Composable
fun MyMealPhoneLayout(
    modifier: Modifier = Modifier,
    state: MyMealUIState,
    totalMacros: TotalMacros,
    onBack: () -> Unit = {},
    onRootChanged: (Boolean) -> Unit,
    onMealNameChange: (String) -> Unit,
    onCreateMealClick: () -> Unit,
) {
    val isOnList = state.destination is MyMealDestination.MyMealList

    LaunchedEffect(isOnList) {
        onRootChanged(isOnList)
    }
    BackHandler(enabled = !isOnList) {
        onBack()
    }

    when(state.destination) {

        MyMealDestination.MyMealList -> {
            MyMealListScreen(
                modifier = modifier,
                meals = state.meals,
                onCreateClick = onCreateMealClick
            )
        }

        MyMealDestination.Create -> {
            CreateMealScreen(
                mealName = state.mealName,
                onMealNameChanged = onMealNameChange,
                ingredients = state.ingredient,
                totalCal = totalMacros.calories,
                totalProtein = totalMacros.protein,
                totalCarbs = totalMacros.carbs,
                totalFat = totalMacros.fat,
                onBack = onBack,
            )
        }

        else -> Unit

    }

}