package com.example.responsiveapp.presentation.home.componet

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.responsiveapp.presentation.home.model.DateItemUiModel
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme

private val SelectedElevation = 6.dp
private val ContentVerticalPadding = 8.dp
private val WeekdayFontSize = 11.sp

@Composable
fun DateItem(
    day: DateItemUiModel,
    dimensions: DatePickerDimensions,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {

    val backgroundColor by animateColorAsState(
        targetValue = if (day.isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.background
        },
        animationSpec = tween(220),
        label = "dateItemBackground",
    )

    val contentColor =
        if (day.isSelected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurface
        }

    val shape = RoundedCornerShape(dimensions.cornerRadius)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Column(
            modifier = Modifier
                .size(
                    width = dimensions.itemWidth,
                    height = dimensions.itemHeight,
                )
                .alpha(
                    if (day.isFuture) 0.35f else 1f
                )
                .shadow(
                    elevation = if (day.isSelected) SelectedElevation else 0.dp,
                    shape = shape,
                )
                .border(
                    width = if (day.isToday && !day.isSelected) 1.dp else 0.dp,
                    color = if (day.isToday && !day.isSelected) {
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.85f
                        )
                    } else {
                        Color.Transparent
                    },
                    shape = shape,
                )
                .clip(shape)
                .background(
                    if (day.isSelected) {
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                            )
                        )
                    } else {
                        Brush.verticalGradient(
                            listOf(
                                backgroundColor,
                                backgroundColor,
                            )
                        )
                    }
                )
                .clickable(
                    enabled = !day.isFuture,
                    onClick = { onClick(day.epochMillis) },
                )
                .padding(
                    horizontal = dimensions.contentPadding,
                    vertical = ContentVerticalPadding,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Text(
                text = day.weekdayLabel,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = WeekdayFontSize,
                ),
                color = contentColor.copy(
                    alpha = if (day.isSelected) 0.85f else 0.55f,
                ),
            )

            Text(
                text = day.dayLabel,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = contentColor,
            )
        }

    }
}

@Preview
@Composable
private fun PrevDateIem() {
    ResponsiveAppTheme {
        DateItem(
            day = DateItemUiModel(
                epochMillis = System.currentTimeMillis(),
                weekdayLabel = "Mon",
                dayLabel = "20",
                isSelected = false,
                isToday = true,
                isFuture = false,
            ),
            dimensions = calculateDatePickerDimensions(
                availableWidth = 360.dp
            ),
            onClick = {},
        )
    }
}