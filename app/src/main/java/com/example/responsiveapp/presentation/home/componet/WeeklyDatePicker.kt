package com.example.responsiveapp.presentation.home.componet

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.core.utils.DateUtils
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

private val SwipeThreshold = 72.dp

@Composable
fun WeeklyDatePicker(
    weekStartDate: Long,
    weekDays: List<Long>,
    selectedDate: Long,
    onDateSelected: (Long) -> Unit,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
    onGoToToday: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val isCurrentWeek = remember(weekStartDate) {
        DateUtils.isCurrentWeek(weekStartDate)
    }

    val swipeThresholdPx = with(LocalDensity.current) {
        SwipeThreshold.toPx()
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.md)
            .pointerInput(weekStartDate) {

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
                                    !isCurrentWeek -> {
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
                    .fillMaxWidth()
                    .padding(
                        vertical = MaterialTheme.spacing.sm,
                    ),
            contentAlignment = Alignment.Center
            ) {

                WeeklyDateRow(
                    weekDays = weekDays,
                    selectedDate = selectedDate,
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
            weekStartDate = System.currentTimeMillis(),
            weekDays = listOf(
                System.currentTimeMillis(),
                System.currentTimeMillis() + 1000 * 60 * 60 * 24,
                System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 2,
                System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 3,
                System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 4,
                System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 5,
                System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 6,
            ),
            selectedDate = System.currentTimeMillis(),
            onDateSelected = {},
            onPreviousWeek = {},
            onNextWeek = {},
            onGoToToday = {},
        )
    }

}