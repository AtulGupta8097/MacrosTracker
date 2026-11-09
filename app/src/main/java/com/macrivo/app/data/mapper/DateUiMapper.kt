package com.macrivo.app.data.mapper

import com.macrivo.app.core.utils.DateUtils
import com.macrivo.app.presentation.home.model.DateItemUiModel
import com.macrivo.app.presentation.home.model.DatePickerUiState
import com.macrivo.app.presentation.home.model.WeekUiState

data class DateSelectionState(
    val selectedDate: Long,
    val weekStartDate: Long,
    val weekDays: List<Long>,
)

fun DateSelectionState.toDatePickerUiState(): DatePickerUiState {

    val days = weekDays.map { date ->
        DateItemUiModel(
            epochMillis = date,
            weekdayLabel = DateUtils.formatWeekdayLabel(date),
            dayLabel = DateUtils.formatDayOfMonth(date),
            isSelected = DateUtils.isSameDay(date, selectedDate),
            isToday = DateUtils.isToday(date),
            isFuture = DateUtils.isFutureDate(date),
        )
    }

    return DatePickerUiState(
        week = WeekUiState(
            weekStartDate = weekStartDate,
            days = days,
        ),
        isCurrentWeek = DateUtils.isCurrentWeek(weekStartDate),
    )
}
