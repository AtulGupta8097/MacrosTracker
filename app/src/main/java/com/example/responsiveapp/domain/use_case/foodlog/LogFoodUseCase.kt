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

    operator fun invoke(foodLog: FoodLog): Flow<Resource<FoodLog>> = flow {
        emit(Resource.Loading())

        try {
            val logResult = foodLogRepository.logFood(foodLog)

            val savedLog = logResult.getOrElse {
                emit(Resource.Error(it.message ?: "Failed to log food"))
                return@flow
            }

            updateDailySummary(savedLog).getOrElse {
                emit(Resource.Error(it.message ?: "Failed to update daily summary"))
                return@flow
            }

            emit(Resource.Success(savedLog))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unexpected error"))
        }
    }
}