package com.example.responsiveapp.domain.use_case.streak

import com.example.responsiveapp.domain.calculator.StreakCalculator
import com.example.responsiveapp.domain.model.streak.StreakRange
import com.example.responsiveapp.domain.model.streak.StreakStats
import com.example.responsiveapp.domain.repository.FoodLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveStreakStatsUseCase @Inject constructor(
    private val foodLogRepository: FoodLogRepository,
    private val streakCalculator: StreakCalculator,
) {
    operator fun invoke(range: StreakRange = StreakRange.ALL_TIME): Flow<StreakStats> =
        foodLogRepository.observeLoggedDates().map { loggedDates ->
            streakCalculator.calculate(
                loggedDates = loggedDates,
                range = range,
            )
        }
}
