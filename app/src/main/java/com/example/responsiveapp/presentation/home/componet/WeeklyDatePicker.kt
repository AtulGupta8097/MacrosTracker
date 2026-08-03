package com.example.responsiveapp.presentation.home.componet

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.home.model.DateItemUiModel
import com.example.responsiveapp.presentation.home.model.DatePickerUiState
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

private val SwipeThreshold = 72.dp

@Composable
fun WeeklyDatePicker(
    modifier: Modifier = Modifier,
    datePickerUiState: DatePickerUiState,
    onDateSelected: (Long) -> Unit,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
) {

    val swipeThresholdPx = with(LocalDensity.current) {
        SwipeThreshold.toPx()
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.md)
            .pointerInput(datePickerUiState.days) {

                var dragAccumulator = 0f

                detectHorizontalDragGestures(

                    onDragStart = {
                        dragAccumulator = 0f
                    },

                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        dragAccumulator += dragAmount
                    },

                    onDragEnd = {

                        when {

                            dragAccumulator > swipeThresholdPx -> {
                                onPreviousWeek()
                            }

                            dragAccumulator < -swipeThresholdPx &&
                                    !datePickerUiState.isCurrentWeek -> {
                                onNextWeek()
                            }
                        }
                    }
                )
            },
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 2.dp,
    ) {

        BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth(),
            contentAlignment = Alignment.Center
            ) {

                WeeklyDateRow(
                    days = datePickerUiState.days,
                    dimensions = calculateDatePickerDimensions(maxWidth),
                    onDateSelected = onDateSelected,
                )
            }
        }
}

@Preview(showBackground = true)
@Composable
private fun PrevDatePicker() {
    ResponsiveAppTheme {
        WeeklyDatePicker(
            datePickerUiState = DatePickerUiState(
                days = listOf(
                    DateItemUiModel(
                        epochMillis = System.currentTimeMillis(),
                        weekdayLabel = "Mon",
                        dayLabel = "20",
                        isSelected = true,
                        isToday = true,
                        isFuture = false,
                    ),
                    DateItemUiModel(
                        epochMillis = System.currentTimeMillis() + 1000 * 60 * 60 * 24,
                        weekdayLabel = "Tue",
                        dayLabel = "21",
                        isSelected = false,
                        isToday = false,
                        isFuture = false,
                    ),
                ),
                isCurrentWeek = true,
            ),
            onDateSelected = {},
            onPreviousWeek = {},
            onNextWeek = {},
        )
    }

}