package com.example.responsiveapp.presentation.mymeal.component

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.responsiveapp.presentation.mymeal.MyMealDestination
import com.example.responsiveapp.presentation.mymeal.MyMealUIState

@Composable
fun MyMealPhoneLayout(
    state: MyMealUIState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onRootChanged: (Boolean) -> Unit,
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

        }

        else -> Unit

    }

}