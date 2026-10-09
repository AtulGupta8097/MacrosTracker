package com.example.responsiveapp.domain.use_case.foodlog

import com.example.responsiveapp.domain.repository.FoodLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLoggedDatesUseCase @Inject constructor(
    private val foodLogRepository: FoodLogRepository,
) {
    operator fun invoke(): Flow<List<Long>> =
        foodLogRepository.observeLoggedDates()
}
