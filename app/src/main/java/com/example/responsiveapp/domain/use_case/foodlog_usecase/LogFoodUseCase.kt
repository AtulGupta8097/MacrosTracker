package com.example.responsiveapp.domain.use_case.foodlog_usecase

import com.example.responsiveapp.core.utils.Resource
import com.example.responsiveapp.data.mapper.toEntity
import com.example.responsiveapp.domain.model.FoodLog
import com.example.responsiveapp.domain.model.LogFoodRequest
import com.example.responsiveapp.domain.repository.FoodLogRepository
import com.example.responsiveapp.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LogFoodUseCase @Inject constructor(
    private val foodLogRepository: FoodLogRepository,
) {

    operator fun invoke(
        foodLog: FoodLog
    ): Flow<Resource<FoodLog>> = flow {

        emit(Resource.Loading())

        try {
            val saveResult = foodLogRepository.logFood(foodLog)
            saveResult.fold(
                onSuccess = { emit(Resource.Success(it)) },
                onFailure = {
                    emit(Resource.Error(it.message ?: "Failed to log food"))
                }
            )

        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unexpected error"))
        }
    }
}
