package com.example.responsiveapp.domain.calculator

import com.example.responsiveapp.core.utils.DateUtils
import com.example.responsiveapp.domain.model.streak.StreakRange
import com.example.responsiveapp.domain.model.streak.StreakStats
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class StreakCalculator @Inject constructor() {

    fun calculate(
        loggedDates: List<Long>,
        range: StreakRange = StreakRange.ALL_TIME,
        referenceDate: Long = DateUtils.today(),
    ): StreakStats {

        val loggedDaySet = loggedDates.toSet()

        val loggedDaysInRange =
            loggedDaysInRange(
                loggedDates = loggedDaySet,
                range = range,
                referenceDate = referenceDate,
            )

        return StreakStats(
            currentStreak = currentStreak(
                loggedDates = loggedDaySet,
                referenceDate = referenceDate,
            ),
            longestStreak = longestStreak(
                loggedDates = loggedDaySet,
            ),
            loggedDaysInRange = loggedDaysInRange,
            consistencyPercentage = consistency(
                loggedDates = loggedDaySet,
                loggedDays = loggedDaysInRange,
                range = range,
                referenceDate = referenceDate,
            ),
            range = range,
        )
    }

    private fun currentStreak(
        loggedDates: Set<Long>,
        referenceDate: Long,
    ): Int {

        var cursor = referenceDate

        if (referenceDate !in loggedDates) {
            cursor = DateUtils.minusDays(
                referenceDate,
                1,
            )

            if (cursor !in loggedDates) {
                return 0
            }
        }

        var streak = 0

        while (cursor in loggedDates) {
            streak++

            cursor = DateUtils.minusDays(
                cursor,
                1,
            )
        }

        return streak
    }

    private fun longestStreak(
        loggedDates: Set<Long>,
    ): Int {

        if (loggedDates.isEmpty()) {
            return 0
        }

        val sortedDates = loggedDates.sorted()

        var longest = 1
        var current = 1

        for (index in 1 until sortedDates.size) {

            val isConsecutive =
                DateUtils.daysBetween(
                    sortedDates[index - 1],
                    sortedDates[index],
                ) == 1L

            current =
                if (isConsecutive) {
                    current + 1
                } else {
                    1
                }

            longest = maxOf(
                longest,
                current,
            )
        }

        return longest
    }

    /**
     * Counts the number of logged calendar days inside the selected
     * [range].
     */
    private fun loggedDaysInRange(
        loggedDates: Set<Long>,
        range: StreakRange,
        referenceDate: Long,
    ): Int {

        val rangeStart =
            rangeStart(
                loggedDates = loggedDates,
                range = range,
                referenceDate = referenceDate,
            ) ?: return 0

        return loggedDates.count { date ->
            !date.isBefore(rangeStart) &&
                !date.isAfter(referenceDate)
        }
    }

    private fun consistency(
        loggedDates: Set<Long>,
        loggedDays: Int,
        range: StreakRange,
        referenceDate: Long,
    ): Int {

        val rangeStart =
            rangeStart(
                loggedDates = loggedDates,
                range = range,
                referenceDate = referenceDate,
            ) ?: return 0

        val totalDays =
            DateUtils.daysBetween(
                rangeStart,
                referenceDate,
            ) + 1

        if (totalDays <= 0) {
            return 0
        }

        return (
            loggedDays / totalDays.toFloat() * 100
        )
            .roundToInt()
            .coerceIn(0, 100)
    }

    private fun rangeStart(
        loggedDates: Set<Long>,
        range: StreakRange,
        referenceDate: Long,
    ): Long? =
        when (val windowDays = range.days) {

            null -> {
                loggedDates.minOrNull()
            }

            else -> {
                DateUtils.minusDays(
                    referenceDate,
                    (windowDays - 1).toLong(),
                )
            }
        }

    private fun Long.isBefore(
        other: Long,
    ): Boolean = this < other

    private fun Long.isAfter(
        other: Long,
    ): Boolean = this > other
}