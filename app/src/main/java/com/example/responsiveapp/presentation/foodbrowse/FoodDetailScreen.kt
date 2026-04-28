package com.example.responsiveapp.presentation.foodbrowse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.presentation.foodbrowse.component.ErrorState
import com.example.responsiveapp.presentation.foodbrowse.component.FoodDetailContent
import com.example.responsiveapp.presentation.ui.theme.spacing
import kotlin.math.roundToInt

@Composable
fun FoodDetailScreen(
    uiState: FoodDetailUiState,
    onBack: () -> Unit,
    onServingSelected: (Serving) -> Unit,
    onQuantityChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is FoodDetailUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                IconButton(
                    onClick = onBack,
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
                    onBack = onBack,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is FoodDetailUiState.Success -> {
                FoodDetailContent(
                    foodName = uiState.foodName,
                    foodBrand = uiState.foodBrand,
                    foodServing = uiState.servings,
                    selectedServing = uiState.selectedServing,
                    quantity = uiState.quantity,
                    scaledNutrition = uiState.scaledNutrition,
                    onServingSelected = onServingSelected,
                    onQuantityChanged = onQuantityChanged,
                    onBack = onBack,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

fun Float.fmt(): String =
    if (this < 10f) "%.1f".format(this) else "${roundToInt()}"