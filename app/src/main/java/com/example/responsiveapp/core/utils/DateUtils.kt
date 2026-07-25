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

    fun todayEpochMillis(): Long =
        LocalDate.now(zone).atStartOfDay(zone).toInstant().toEpochMilli()

    fun getStartOfWeek(epochMillis: Long): Long {
        val date = epochMillis.toLocalDate()
        val startOfWeek = date.with(DayOfWeek.MONDAY)
        return startOfWeek.toEpochMillis()
    }

    fun getWeekDates(weekStartEpochMillis: Long): List<Long> {
        val start = weekStartEpochMillis.toLocalDate()
        return (0..6).map { offset -> start.plusDays(offset.toLong()).toEpochMillis() }
    }

    fun getPreviousWeekStart(currentWeekStartEpochMillis: Long): Long =
        currentWeekStartEpochMillis.toLocalDate().minusWeeks(1).toEpochMillis()

    fun getNextWeekStart(currentWeekStartEpochMillis: Long): Long =
        currentWeekStartEpochMillis.toLocalDate().plusWeeks(1).toEpochMillis()

    fun formatMonthTitle(epochMillis: Long): String {
        val date = epochMillis.toLocalDate()
        val month = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return "$month ${date.year}"
    }

    fun formatDayName(epochMillis: Long): String =
        epochMillis.toLocalDate().dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())

    fun formatDayNumber(epochMillis: Long): String =
        epochMillis.toLocalDate().dayOfMonth.toString()

    fun formatFullDate(epochMillis: Long): String {
        val date = epochMillis.toLocalDate()
        val weekday = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val month = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return "$weekday, ${date.dayOfMonth} $month"
    }

    fun isCurrentWeek(weekStartEpochMillis: Long): Boolean =
        weekStartEpochMillis == getStartOfWeek(todayEpochMillis())

    fun isDateInWeek(dateEpochMillis: Long, weekStartEpochMillis: Long): Boolean {
        val date = dateEpochMillis.toLocalDate()
        val start = weekStartEpochMillis.toLocalDate()
        val end = start.plusDays(6)
        return !date.isBefore(start) && !date.isAfter(end)
    }

    fun isToday(epochMillis: Long): Boolean =
        epochMillis.toLocalDate() == LocalDate.now(zone)

    fun isSameDay(a: Long, b: Long): Boolean =
        a.toLocalDate() == b.toLocalDate()

    private fun Long.toLocalDate(): LocalDate =
        Instant.ofEpochMilli(this).atZone(zone).toLocalDate()

    private fun LocalDate.toEpochMillis(): Long =
        atStartOfDay(zone).toInstant().toEpochMilli()


    private val DATE_KEY_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun Long.toLocalDateKey(
        zoneId: ZoneId = ZoneId.systemDefault()
    ): String {

        return Instant
            .ofEpochMilli(this)
            .atZone(zoneId)
            .toLocalDate()
            .format(DATE_KEY_FORMATTER)
    }
}
