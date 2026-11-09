package com.macrivo.app.presentation.foodbrowse

import com.macrivo.app.domain.model.Serving

sealed interface FoodDetailUiEvent {

    data object Back : FoodDetailUiEvent

    data class ServingSelected(val serving: Serving) : FoodDetailUiEvent

    data class QuantityChanged(val quantity: Float) : FoodDetailUiEvent

    data object SaveToggled : FoodDetailUiEvent

    data object LogClicked : FoodDetailUiEvent

    /** LOGGED_TODAY only: store the edited quantity. */
    data object SaveChangesClicked : FoodDetailUiEvent

    /** Sent only after the user confirmed the delete dialog (LOGGED_TODAY mode). */
    data object DeleteConfirmed : FoodDetailUiEvent
}
