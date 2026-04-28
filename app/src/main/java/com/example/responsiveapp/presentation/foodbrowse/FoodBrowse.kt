package com.example.responsiveapp.presentation.foodbrowse

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.responsiveapp.presentation.ui.theme.DeviceConfiguration
import com.example.responsiveapp.presentation.ui.theme.deviceConfiguration

@Composable
fun FoodBrowse(
    viewModel: FoodBrowseViewModel = hiltViewModel(),
    onRootChanged: (Boolean) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val foodDetailUiState by viewModel.foodDetailUiState.collectAsStateWithLifecycle()
    val deviceConfiguration = MaterialTheme.deviceConfiguration

    when(deviceConfiguration) {
        DeviceConfiguration.MOBILE -> {
            FoodBrowsePhoneLayout(
                state = state,
                foodDetailUiState = foodDetailUiState,
                onRootChanged = onRootChanged,
                onQueryChange = viewModel::onQueryChange,
                onFoodClick = viewModel::openFoodDetail,
                onBack = viewModel::onBackFromDetail,
                onServingSelected = viewModel::onServingSelected,
                onQuantityChanged = viewModel::onQuantityChanged,
            )
        }
        DeviceConfiguration.TABLET -> {
            FoodBrowseTabletLayout(
                state = state,
                foodDetailUiState = foodDetailUiState,
                onQueryChange = viewModel::onQueryChange,
                onFoodClick = viewModel::openFoodDetail,
                onFirstVisibleFoodChanged = viewModel::onFirstVisibleFoodChanged,
                onServingSelected = viewModel::onServingSelected,
                onQuantityChanged = viewModel::onQuantityChanged,
            )
        }

        else -> {
            FoodBrowseTabletLayout(
                state = state,
                foodDetailUiState = foodDetailUiState,
                onQueryChange = viewModel::onQueryChange,
                onFoodClick = viewModel::openFoodDetail,
                onFirstVisibleFoodChanged = viewModel::onFirstVisibleFoodChanged,
                onServingSelected = viewModel::onServingSelected,
                onQuantityChanged = viewModel::onQuantityChanged,
            )
        }
    }
}