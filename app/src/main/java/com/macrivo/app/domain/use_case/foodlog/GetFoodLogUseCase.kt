package com.macrivo.app.domain.use_case.foodlog

import com.macrivo.app.domain.model.foodlog.FoodLog
import com.macrivo.app.domain.repository.FoodLogRepository
import javax.inject.Inject

// CHANGED: new file.
class GetFoodLogUseCase @Inject constructor(
    private val foodLogRepository: FoodLogRepository
) {
    suspend operator fun invoke(id: String): FoodLog? =
        foodLogRepository.getFoodLog(id)
}
