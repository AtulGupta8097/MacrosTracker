package com.macrivo.app.domain.use_case.foodlog

import com.macrivo.app.domain.model.foodlog.FoodLog
import com.macrivo.app.domain.repository.FoodLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveFoodLogsForDateUseCase @Inject constructor(
    private val foodLogRepository: FoodLogRepository
) {
    operator fun invoke(date: Long): Flow<List<FoodLog>> =
        foodLogRepository.observeFoodLogsForDate(date)
}
