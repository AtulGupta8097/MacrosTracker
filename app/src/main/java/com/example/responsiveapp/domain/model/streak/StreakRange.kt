package com.example.responsiveapp.domain.model.streak

/**
 * Window used for the "consistency" part of StreakStats (current/longest
 * streak are always computed over full history regardless of range - a
 * streak isn't meaningful if it resets just because you picked "Week").
 *
 * `days = null` means ALL_TIME: consistency is measured from the very
 * first logged day instead of a fixed window.
 */
enum class StreakRange(val days: Int?) {
    WEEK(7),
    MONTH(30),
    THREE_MONTHS(90),
    YEAR(365),
    ALL_TIME(null),
}
