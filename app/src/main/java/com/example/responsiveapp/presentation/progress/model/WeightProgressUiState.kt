package com.example.responsiveapp.presentation.progress.model

import androidx.compose.runtime.Immutable

@Immutable
data class WeightProgressUiState(
    val hasData: Boolean = false,
    val points: List<WeightChartPointUi> = emptyList(),
    val latestWeight: Float? = null,
)

@Immutable
data class WeightChartPointUi(
    val label: String,
    val weight: Float,
)