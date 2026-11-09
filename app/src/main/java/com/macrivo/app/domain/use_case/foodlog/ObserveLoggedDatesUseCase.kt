package com.macrivo.app.domain.use_case.foodlog

import com.macrivo.app.domain.repository.FoodLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLoggedDatesUseCase @Inject constructor(
    private val foodLogRepository: FoodLogRepository,
) {
    operator fun invoke(): Flow<List<Long>> =
        foodLogRepository.observeLoggedDates()
}
