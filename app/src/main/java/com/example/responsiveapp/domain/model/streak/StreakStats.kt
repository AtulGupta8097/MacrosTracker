package com.example.responsiveapp.domain.model.streak

data class StreakStats(
    val currentStreak: Int,
    val longestStreak: Int,
    val totalLoggedDays: Int,
    val consistencyPercentage: Int,
    val range: StreakRange,
)
