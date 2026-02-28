package com.example.responsiveapp.presentation.food_browse

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.responsiveapp.domain.model.FoodDetail

@Composable
fun FoodBrowse(
    viewModel: FoodBrowseViewModel = hiltViewModel(),
    onRootChanged: (Boolean) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.destination) {
        onRootChanged(state.destination is FoodBrowseDestination.List)
    }

    BackHandler(enabled = state.destination !is FoodBrowseDestination.List) {
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
                onFoodClick = viewModel::openFoodDetail
            )
        }

        is FoodBrowseDestination.Detail -> {
            FoodDetailScreen(
                food = state.selectedFood,
                isLoading = state.isDetailLoading,
                onBack = viewModel::onBackFromDetail
            )
        }
    }
}

@Composable
fun FoodDetailScreen(
    food: FoodDetail?,
    isLoading: Boolean,
    onBack: () -> Unit
) {
    Text("FoodDetails")
}