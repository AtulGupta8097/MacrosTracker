package com.example.responsiveapp.core.utils

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object DateUtils {

    private val zone: ZoneId = ZoneId.systemDefault()
    private const val DAYS_PER_WEEK = 7

    private val DATE_KEY_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private val TIME_OF_DAY_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ofPattern("h:mm a")

    fun today(): Long =
        LocalDate.now(zone).toEpochMillis()

    fun formatTimeOfDay(epochMillis: Long): String =
        Instant.ofEpochMilli(epochMillis).atZone(zone).toLocalTime().format(TIME_OF_DAY_FORMATTER)

    fun getWeekStart(date: Long): Long =
        date.toLocalDate().with(DayOfWeek.MONDAY).toEpochMillis()

    fun getCurrentWeekDates(weekStart: Long): List<Long> {
        val start = weekStart.toLocalDate()
        return (0 until DAYS_PER_WEEK).map { offset -> start.plusDays(offset.toLong()).toEpochMillis() }
    }

    fun getPreviousWeek(weekStart: Long): Long =
        weekStart.toLocalDate().minusWeeks(1).toEpochMillis()

    fun getNextWeek(weekStart: Long): Long =
        weekStart.toLocalDate().plusWeeks(1).toEpochMillis()

    fun getSelectedWeekdayIndex(date: Long, weekStart: Long): Int {
        val daysBetween = java.time.temporal.ChronoUnit.DAYS.between(weekStart.toLocalDate(), date.toLocalDate())
        return daysBetween.toInt().coerceIn(0, DAYS_PER_WEEK - 1)
    }

    fun getDateForWeekday(weekStart: Long, weekdayIndex: Int): Long =
        weekStart.toLocalDate().plusDays(weekdayIndex.toLong()).toEpochMillis()

    fun formatMonthTitle(date: Long): String {
        val localDate = date.toLocalDate()
        val month = localDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return "$month ${localDate.year}"
    }

    fun formatWeekdayLabel(date: Long): String =
        date.toLocalDate().dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())

    fun formatDayOfMonth(date: Long): String =
        date.toLocalDate().dayOfMonth.toString()

    fun formatFullDate(date: Long): String {
        val localDate = date.toLocalDate()
        val weekday = localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val month = localDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return "$weekday, ${localDate.dayOfMonth} $month"
    }

    fun isCurrentWeek(weekStart: Long): Boolean =
        weekStart == getWeekStart(today())

    fun isDateInWeek(date: Long, weekStart: Long): Boolean {
        val localDate = date.toLocalDate()
        val start = weekStart.toLocalDate()
        val end = start.plusDays((DAYS_PER_WEEK - 1).toLong())
        return !localDate.isBefore(start) && !localDate.isAfter(end)
    }

    fun isToday(date: Long): Boolean =
        date.toLocalDate() == LocalDate.now(zone)

    fun isFutureDate(date: Long): Boolean =
        date.toLocalDate().isAfter(LocalDate.now(zone))

    fun isSameDay(a: Long, b: Long): Boolean =
        a.toLocalDate() == b.toLocalDate()

    fun Long.toLocalDateKey(zoneId: ZoneId = zone): String =
        Instant.ofEpochMilli(this).atZone(zoneId).toLocalDate().format(DATE_KEY_FORMATTER)

    private fun Long.toLocalDate(): LocalDate =
        Instant.ofEpochMilli(this).atZone(zone).toLocalDate()

    private fun LocalDate.toEpochMillis(): Long =
        atStartOfDay(zone).toInstant().toEpochMilli()
}
