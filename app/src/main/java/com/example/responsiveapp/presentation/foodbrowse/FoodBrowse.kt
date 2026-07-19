package com.example.responsiveapp.presentation.foodbrowse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.responsiveapp.presentation.commoncomponent.CustomToast
import com.example.responsiveapp.presentation.foodbrowse.component.FoodBrowseEvent
import com.example.responsiveapp.presentation.ui.theme.DeviceConfiguration
import com.example.responsiveapp.presentation.ui.theme.deviceConfiguration


@Composable
fun FoodBrowse(
    viewModel: FoodBrowseViewModel = hiltViewModel(),
    onRootChanged: (Boolean) -> Unit,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val detailState by viewModel.foodDetailUiState.collectAsStateWithLifecycle()

    val deviceConfiguration = MaterialTheme.deviceConfiguration


    LaunchedEffect(Unit) {

        viewModel.events.collect { event ->

            when (event) {

                FoodBrowseEvent.FoodLogged -> {
                    viewModel.onBackFromDetail()
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        when (deviceConfiguration) {

            DeviceConfiguration.MOBILE -> {
                FoodBrowsePhoneLayout(
                    state = state,
                    foodDetailUiState = detailState,
                    onRootChanged = onRootChanged,
                    onQueryChange = viewModel::onQueryChange,
                    onFoodClick = viewModel::openFoodDetail,
                    onBack = viewModel::onBackFromDetail,
                    onServingSelected = viewModel::onServingSelected,
                    onQuantityChanged = viewModel::onQuantityChanged,
                    onLog = viewModel::logFoodClicked
                )
            }

            else -> {
                FoodBrowseTabletLayout(
                    state = state,
                    foodDetailUiState = detailState,
                    onQueryChange = viewModel::onQueryChange,
                    onFoodClick = viewModel::openFoodDetail,
                    onFirstVisibleFoodChanged = viewModel::onFirstVisibleFoodChanged,
                    onServingSelected = viewModel::onServingSelected,
                    onQuantityChanged = viewModel::onQuantityChanged,
                    onLog = viewModel::logFoodClicked
                )
            }
        }

        CustomToast(
            message = state.toast.message,
            type = state.toast.type,
            durationMillis = state.toast.duration,
            visibility = state.toast.visible,
            onDismiss = viewModel::hideToast
        )
    }
}