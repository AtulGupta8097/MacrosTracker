package com.example.responsiveapp.presentation.food_browse

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun FoodBrowse(
    viewModel: FoodBrowseViewModel = hiltViewModel(),
    onRootChanged: (Boolean) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val foodDetailUiState by viewModel.foodDetailUiState.collectAsStateWithLifecycle()
    val isOnList = state.destination is FoodBrowseDestination.List

    LaunchedEffect(isOnList) {
        onRootChanged(isOnList)
    }

    BackHandler(enabled = !isOnList) {
        viewModel.onBackFromDetail()
    }

    when (state.destination) {
        FoodBrowseDestination.List -> {
            FoodBrowseListScreen(
                query = state.query,
                onQueryChange = viewModel::onQueryChange,
                data = state.foods,
                isVertical = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT,
                isLoading = state.isLoading,
                onFoodClick = viewModel::openFoodDetail,
            )
        }

        is FoodBrowseDestination.Detail -> {
            FoodDetailScreen(
                uiState = foodDetailUiState,
                onBack = viewModel::onBackFromDetail,
                onServingSelected = viewModel::onServingSelected,
                onQuantityChanged = viewModel::onQuantityChanged,
            )
        }
    }
}