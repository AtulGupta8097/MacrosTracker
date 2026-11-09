package com.macrivo.app.presentation.progress.mapper

import com.macrivo.app.core.utils.DateUtils
import com.macrivo.app.domain.model.weight.WeightLog
import com.macrivo.app.presentation.progress.model.WeightChartPointUi
import com.macrivo.app.presentation.progress.model.WeightProgressUiState

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
        logCount = points.size,
    )
}