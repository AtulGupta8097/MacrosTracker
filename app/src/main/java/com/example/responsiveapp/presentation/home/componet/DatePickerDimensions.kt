package com.example.responsiveapp.presentation.home.componet

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp

@Immutable
data class DatePickerDimensions(
    val itemWidth: Dp,
    val itemHeight: Dp,
    val itemSpacing: Dp,
    val cornerRadius: Dp,
    val contentPadding: Dp,
)

fun calculateDatePickerDimensions(availableWidth: Dp): DatePickerDimensions {
    val daysPerWeek = 7
    val spacing = when {
        availableWidth < 360.dp -> 6.dp
        availableWidth < 600.dp -> 8.dp
        else -> 12.dp
    }

    val totalSpacing = spacing * (daysPerWeek - 1)

    val contentPadding = when {
        availableWidth < 360.dp -> 4.dp
        availableWidth < 600.dp -> 6.dp
        else -> 8.dp
    }

    val itemWidth =
        ((availableWidth - totalSpacing) / daysPerWeek)
            .coerceAtMost( 64.dp)

    val itemHeight = itemWidth * 1.7f

    val cornerRadius = itemWidth * 0.32f
    return DatePickerDimensions(
        itemWidth = itemWidth,
        itemHeight = itemHeight,
        itemSpacing = spacing,
        cornerRadius = cornerRadius,
        contentPadding = contentPadding,
    )
}
