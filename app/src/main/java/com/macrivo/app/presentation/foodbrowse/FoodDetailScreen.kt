package com.macrivo.app.presentation.foodbrowse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.macrivo.app.presentation.commoncomponent.ConfirmDeleteFoodDialog
import com.macrivo.app.presentation.foodbrowse.component.ErrorState
import com.macrivo.app.presentation.foodbrowse.component.FoodDetailContent
import com.macrivo.app.presentation.ui.theme.spacing
import kotlin.math.roundToInt

@Composable
fun FoodDetailScreen(
    modifier: Modifier = Modifier,
    uiState: FoodDetailUiState,
    onEvent: (FoodDetailUiEvent) -> Unit,
    snackbarHostState: SnackbarHostState? = null,
) {
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    if (showDeleteDialog && uiState is FoodDetailUiState.Success &&
        uiState.mode == FoodDetailMode.LOGGED_TODAY
    ) {
        ConfirmDeleteFoodDialog(
            foodName = uiState.foodName,
            isDeleting = uiState.isBusy,
            onConfirm = {
                showDeleteDialog = false
                onEvent(FoodDetailUiEvent.DeleteConfirmed)
            },
            onDismiss = { showDeleteDialog = false },
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is FoodDetailUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                IconButton(
                    onClick = { onEvent(FoodDetailUiEvent.Back) },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .statusBarsPadding()
                        .padding(MaterialTheme.spacing.sm),
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }

            is FoodDetailUiState.Error -> {
                ErrorState(
                    error = uiState.message,
                    onBack = { onEvent(FoodDetailUiEvent.Back) },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is FoodDetailUiState.Success -> {
                FoodDetailContent(
                    modifier = Modifier.fillMaxSize(),
                    foodName = uiState.foodName,
                    foodBrand = uiState.foodBrand,
                    foodServing = uiState.servings,
                    nutritionLabel = uiState.nutritionLabel,
                    selectedServing = uiState.selectedServing,
                    quantity = uiState.quantity,
                    scaledNutrition = uiState.scaledNutrition,
                    onServingSelected = { onEvent(FoodDetailUiEvent.ServingSelected(it)) },
                    onQuantityChanged = { onEvent(FoodDetailUiEvent.QuantityChanged(it)) },
                    onBack = { onEvent(FoodDetailUiEvent.Back) },
                    isSaved = uiState.isSaved,
                    onSaveToggle = { onEvent(FoodDetailUiEvent.SaveToggled) },
                    onLog = { onEvent(FoodDetailUiEvent.LogClicked) },
                    mode = uiState.mode,
                    onDelete = { showDeleteDialog = true },
                    isBusy = uiState.isBusy,
                    hasChanges = uiState.hasChanges,
                    onSaveChanges = { onEvent(FoodDetailUiEvent.SaveChangesClicked) },
                )
            }
        }

        if (snackbarHostState != null) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding(),
            )
        }
    }
}

fun Float.fmt(): String =
    if (this < 10f) "%.1f".format(this) else "${roundToInt()}"

