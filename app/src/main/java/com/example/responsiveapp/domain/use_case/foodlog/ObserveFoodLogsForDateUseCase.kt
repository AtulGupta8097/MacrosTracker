package com.example.responsiveapp.domain.use_case.foodlog

import com.example.responsiveapp.domain.model.foodlog.FoodLog
import com.example.responsiveapp.domain.repository.FoodLogRepository
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
