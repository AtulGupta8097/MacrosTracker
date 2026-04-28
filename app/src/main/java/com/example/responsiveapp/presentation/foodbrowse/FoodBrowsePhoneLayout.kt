package com.example.responsiveapp.presentation.foodbrowse

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.responsiveapp.domain.model.Serving

@Composable
fun FoodBrowsePhoneLayout(
    state: FoodBrowseState,
    foodDetailUiState: FoodDetailUiState,
    onRootChanged: (Boolean) -> Unit,
    onQueryChange: (String) -> Unit,
    onFoodClick: (String) -> Unit,
    onBack: () -> Unit,
    onServingSelected: (Serving) -> Unit,
    onQuantityChanged: (Float) -> Unit,
) {
    val isOnList = state.destination is FoodBrowseDestination.List

    LaunchedEffect(isOnList) { onRootChanged(isOnList) }

    BackHandler(enabled = !isOnList) { onBack() }

    when (state.destination) {
        FoodBrowseDestination.List -> {
            FoodBrowseListScreen(
                query = state.query,
                onQueryChange = onQueryChange,
                data = state.foods,
                isLoading = state.isLoading,
                onFoodClick = onFoodClick,
                selectedFoodId = state.selectedFood?.id,
            )
        }

        is FoodBrowseDestination.Detail -> {
            FoodDetailScreen(
                uiState = foodDetailUiState,
                onBack = onBack,
                onServingSelected = onServingSelected,
                onQuantityChanged = onQuantityChanged,
            )
        }
    }
}