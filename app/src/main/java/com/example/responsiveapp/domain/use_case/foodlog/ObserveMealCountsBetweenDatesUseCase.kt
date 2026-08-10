package com.example.responsiveapp.domain.use_case.foodlog

import com.example.responsiveapp.domain.repository.FoodLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMealCountsBetweenDatesUseCase @Inject constructor(
    private val foodLogRepository: FoodLogRepository
) {

    operator fun invoke(
        startDate: Long,
        endDate: Long
    ): Flow<Map<Long, Int>> {
        return foodLogRepository.observeMealCountsBetweenDates(
            startDate = startDate,
            endDate = endDate
        )
    }
}