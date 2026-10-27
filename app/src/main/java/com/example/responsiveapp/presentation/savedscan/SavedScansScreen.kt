package com.example.responsiveapp.presentation.savedscan

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.responsiveapp.domain.model.FoodDetail
import com.example.responsiveapp.domain.model.toFoodItem
import com.example.responsiveapp.presentation.commoncomponent.CustomToast
import com.example.responsiveapp.presentation.commoncomponent.EmptyState
import com.example.responsiveapp.presentation.foodbrowse.FoodDetailScreen
import com.example.responsiveapp.presentation.foodbrowse.component.FoodCard
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun SavedScansScreen(
    viewModel: SavedScansViewModel = hiltViewModel(),
    onRootChanged: (Boolean) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val detailState by viewModel.foodDetailUiState.collectAsStateWithLifecycle()

    val isOnList = state.destination is SavedScansDestination.List

    LaunchedEffect(isOnList) { onRootChanged(isOnList) }

    BackHandler(enabled = !isOnList) { viewModel.onBack() }

    Box(modifier = Modifier.fillMaxSize()) {
        when (state.destination) {
            SavedScansDestination.List -> {
                SavedScansList(
                    isLoading = state.isLoading,
                    foods = state.savedFoods,
                    onFoodClick = viewModel::onFoodClick,
                )
            }

            is SavedScansDestination.Detail -> {
                FoodDetailScreen(
                    uiState = detailState,
                    onBack = viewModel::onBack,
                    onServingSelected = viewModel::onServingSelected,
                    onQuantityChanged = viewModel::onQuantityChanged,
                    onLog = viewModel::logFoodClicked,
                    onSaveToggle = viewModel::onRemoveFromSaved,
                )
            }
        }

        CustomToast(
            message = state.toast.message,
            type = state.toast.type,
            durationMillis = state.toast.duration,
            visibility = state.toast.visible,
            onDismiss = viewModel::hideToast,
        )
    }
}

@Composable
private fun SavedScansList(
    isLoading: Boolean,
    foods: List<FoodDetail>,
    onFoodClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }

            foods.isEmpty() -> {
                EmptyState(
                    title = "No saved foods yet",
                    description = "Tap the bookmark on a food's details to keep it here for quick access later.",
                    icon = Icons.Filled.Bookmark,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding(),
                    contentPadding = PaddingValues(
                        horizontal = MaterialTheme.spacing.md,
                        vertical = MaterialTheme.spacing.sm,
                    ),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
                ) {
                    items(
                        items = foods,
                        key = { it.id },
                        contentType = { "saved_food_item" },
                    ) { food ->
                        FoodCard(
                            food = food.toFoodItem(),
                            onFoodClick = { onFoodClick(food.id) },
                        )
                    }
                }
            }
        }
    }
}
