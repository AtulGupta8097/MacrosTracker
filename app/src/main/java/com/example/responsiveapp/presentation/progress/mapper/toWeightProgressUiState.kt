package com.example.responsiveapp.presentation.progress.mapper

import com.example.responsiveapp.core.utils.DateUtils
import com.example.responsiveapp.domain.model.weight.WeightLog
import com.example.responsiveapp.presentation.progress.model.WeightChartPointUi
import com.example.responsiveapp.presentation.progress.model.WeightProgressUiState

fun List<WeightLog>.toWeightProgressUiState(): WeightProgressUiState {
    if (isEmpty()) return WeightProgressUiState()

    val sorted = sortedBy { it.date }
    val points = sorted.map { log ->
        WeightChartPointUi(
            label = DateUtils.formatShortDate(log.date),
            weight = log.weight,
        )
    }

    return WeightProgressUiState(
        hasData = true,
        points = points,
        latestWeight = sorted.last().weight,
    )
}