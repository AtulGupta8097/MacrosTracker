package com.example.responsiveapp.presentation.food_browse

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.responsiveapp.domain.model.FoodDetail
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.domain.model.scale
import com.example.responsiveapp.presentation.food_browse.component.ErrorState
import com.example.responsiveapp.presentation.food_browse.component.FoodDetailContent
import com.example.responsiveapp.presentation.ui.theme.spacing
import kotlin.math.roundToInt


@Composable
fun FoodDetailScreen(
    foodDetail: FoodDetail?,
    isLoading: Boolean,
    error: String?,
    onBack: () -> Unit
) {
    var selectedServing by remember(foodDetail) {
        mutableStateOf(foodDetail?.defaultServing)
    }
    var quantity by remember { mutableFloatStateOf(1f) }

    val scaledNutrition = remember(selectedServing, quantity) {
        selectedServing?.nutrition?.scale(quantity)
    }

    val onServingSelected: (Serving) -> Unit = { serving ->
        selectedServing = serving
        quantity = 1f
    }
    val onQuantityChanged: (Float) -> Unit = { newQty ->
        quantity = newQty
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

            error != null -> ErrorState(error = error, onBack = onBack, modifier = Modifier.fillMaxSize())

            foodDetail != null && selectedServing != null && scaledNutrition != null -> {
                FoodDetailContent(
                    foodDetail = foodDetail,
                    selectedServing = selectedServing!!,
                    quantity = quantity,
                    scaledNutrition = scaledNutrition,
                    onServingSelected = onServingSelected,
                    onQuantityChanged = onQuantityChanged,
                    onBack = onBack,
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.statusBarsPadding().padding(MaterialTheme.spacing.sm)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        }
    }

}

fun Float.fmt(): String =
    if (this < 10f) "%.1f".format(this) else "${roundToInt()}"
