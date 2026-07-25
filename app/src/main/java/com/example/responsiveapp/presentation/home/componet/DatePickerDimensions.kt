package com.example.responsiveapp.presentation.home.componet

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class DatePickerDimensions(
    val itemWidth: Dp,
    val itemHeight: Dp,
    val selectedItemHeight: Dp,
    val itemSpacing: Dp,
    val cornerRadius: Dp,
)

fun calculateDatePickerDimensions(availableWidth: Dp): DatePickerDimensions {
    val daysPerWeek = 7
    val spacing = when {
        availableWidth < 360.dp -> 4.dp
        availableWidth < 600.dp -> 6.dp
        else -> 10.dp
    }
    val totalSpacing = spacing * (daysPerWeek - 1)
    val itemWidth = ((availableWidth - totalSpacing) / daysPerWeek).coerceIn(36.dp, 72.dp)
    val itemHeight = itemWidth * 1.55f
    val selectedItemHeight = itemHeight * 1.12f
    val cornerRadius = itemWidth * 0.32f
    return DatePickerDimensions(
        itemWidth = itemWidth,
        itemHeight = itemHeight,
        selectedItemHeight = selectedItemHeight,
        itemSpacing = spacing,
        cornerRadius = cornerRadius,
    )
}
