package com.example.responsiveapp.domain.use_case.foodlog

import com.example.responsiveapp.core.utils.Resource
import com.example.responsiveapp.domain.model.FoodLog
import com.example.responsiveapp.domain.repository.FoodLogRepository
import com.example.responsiveapp.domain.use_case.dailysummary.UpdateDailySummaryUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LogFoodUseCase @Inject constructor(
    private val foodLogRepository: FoodLogRepository,
    private val updateDailySummary: UpdateDailySummaryUseCase
) {

    suspend operator fun invoke(foodLog: FoodLog) {

        foodLogRepository.logFood(foodLog)

        updateDailySummary(foodLog)

    }
}