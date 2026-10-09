package com.example.responsiveapp.presentation.home.mapper

import com.example.responsiveapp.core.utils.DateUtils
import com.example.responsiveapp.domain.model.streak.StreakStats
import com.example.responsiveapp.presentation.home.model.StreakDayUiModel
import com.example.responsiveapp.presentation.home.model.StreakWeekUiState

fun buildStreakWeekUiState(
    streakStats: StreakStats,
    loggedDates: List<Long>,
    currentWeekDays: List<Long>,
    today: Long,
): StreakWeekUiState {

    val loggedSet = loggedDates.toSet()

    val days = currentWeekDays.map { date ->
        StreakDayUiModel(
            weekdayLabel = DateUtils.formatWeekdayLabel(date),
            dayNumber = DateUtils.formatDayOfMonth(date),
            isLogged = date in loggedSet,
            isToday = DateUtils.isSameDay(date, today),
            isFuture = DateUtils.isFutureDate(date),
        )
    }

    return StreakWeekUiState(
        currentStreakCount = streakStats.currentStreak,
        currentStreakText = streakStats.currentStreak.toString(),
        days = days,
    )
}
