package com.example.responsiveapp.presentation.home.componet

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import com.example.responsiveapp.presentation.home.model.WeekUiState
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
            .pointerInput(Unit) {

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

            val dimension = calculateDatePickerDimensions(maxWidth)

            AnimatedContent(
                targetState = datePickerUiState.week,
                transitionSpec = {
                    (slideInHorizontally { it / 2 } + fadeIn())
                        .togetherWith(
                            slideOutHorizontally { -it / 2 } + fadeOut()
                        )
                },
                label = "WeekAnimation",
            ) { dateState ->

                WeeklyDateRow(
                    days = dateState.days,
                    dimensions = dimension,
                    onDateSelected = onDateSelected,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrevDatePicker() {
    ResponsiveAppTheme {
        WeeklyDatePicker(
            datePickerUiState = DatePickerUiState(
                week = WeekUiState(
                    weekStartDate = System.currentTimeMillis(),
                    days = listOf(
                        DateItemUiModel(
                            epochMillis = System.currentTimeMillis(),
                            weekdayLabel = "Mon",
                            dayLabel = "18",
                            isSelected = true,
                            isToday = true,
                            isFuture = false,
                        ),
                        DateItemUiModel(
                            epochMillis = System.currentTimeMillis() + 86_400_000L,
                            weekdayLabel = "Tue",
                            dayLabel = "19",
                            isSelected = false,
                            isToday = false,
                            isFuture = false,
                        ),
                        DateItemUiModel(
                            epochMillis = System.currentTimeMillis() + 2 * 86_400_000L,
                            weekdayLabel = "Wed",
                            dayLabel = "20",
                            isSelected = false,
                            isToday = false,
                            isFuture = false,
                        ),
                        DateItemUiModel(
                            epochMillis = System.currentTimeMillis() + 3 * 86_400_000L,
                            weekdayLabel = "Thu",
                            dayLabel = "21",
                            isSelected = false,
                            isToday = false,
                            isFuture = false,
                        ),
                        DateItemUiModel(
                            epochMillis = System.currentTimeMillis() + 4 * 86_400_000L,
                            weekdayLabel = "Fri",
                            dayLabel = "22",
                            isSelected = false,
                            isToday = false,
                            isFuture = false,
                        ),
                        DateItemUiModel(
                            epochMillis = System.currentTimeMillis() + 5 * 86_400_000L,
                            weekdayLabel = "Sat",
                            dayLabel = "23",
                            isSelected = false,
                            isToday = false,
                            isFuture = false,
                        ),
                        DateItemUiModel(
                            epochMillis = System.currentTimeMillis() + 6 * 86_400_000L,
                            weekdayLabel = "Sun",
                            dayLabel = "24",
                            isSelected = false,
                            isToday = false,
                            isFuture = false,
                        ),
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