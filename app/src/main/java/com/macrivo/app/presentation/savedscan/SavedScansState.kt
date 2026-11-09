package com.macrivo.app.presentation.savedscan

import androidx.compose.runtime.Immutable
import com.macrivo.app.domain.calculator.ToastState
import com.macrivo.app.domain.model.FoodDetail
import com.macrivo.app.domain.model.Serving

// CHANGED: new.
@Immutable
data class SavedScansState(
    val savedFoods: List<FoodDetail> = emptyList(),
    val isLoading: Boolean = true,
    val destination: SavedScansDestination = SavedScansDestination.List,
    val selectedFood: FoodDetail? = null,
    val selectedServing: Serving? = null,
    val quantity: Float = 1f,
    val isLogging: Boolean = false,
    val toast: ToastState = ToastState(),
)
