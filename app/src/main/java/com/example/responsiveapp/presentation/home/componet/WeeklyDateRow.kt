package com.example.responsiveapp.presentation.home.componet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.responsiveapp.core.utils.DateUtils

@Composable
fun WeeklyDateRow(
    weekDays: List<Long>,
    selectedDate: Long,
    dimensions: DatePickerDimensions,
    onDateSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(
                dimensions.itemSpacing
            ),
        ) {

            weekDays.forEach { date ->

                DateItem(
                    date = date,
                    isSelected = DateUtils.isSameDay(
                        date,
                        selectedDate,
                    ),
                    isToday = DateUtils.isToday(date),
                    isFuture = DateUtils.isFutureDate(date),
                    dimensions = dimensions,
                    onClick = onDateSelected,
                )
            }
        }
    }
}