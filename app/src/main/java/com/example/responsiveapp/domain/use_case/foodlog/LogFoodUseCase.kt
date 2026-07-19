package com.example.responsiveapp.domain.use_case.foodlog

import com.example.responsiveapp.core.utils.Resource
import com.example.responsiveapp.data.coordinatior.LogFoodCoordinator
import com.example.responsiveapp.domain.model.foodlog.LogFoodRequest
import javax.inject.Inject

class LogFoodUseCase @Inject constructor(
    private val coordinator: LogFoodCoordinator
) {

    suspend operator fun invoke(request: LogFoodRequest): Resource<Unit> {

        if (request.quantity <= 0f) {
            return Resource.Error("Quantity must be greater than zero.")
        }

        return try {
            coordinator.logFood(request)
            Resource.Success(Unit)
        } catch (e: IllegalStateException) {
            Resource.Error(
                e.message ?: "Unable to log food."
            )
        } catch (e: Exception) {
            Resource.Error(
                "Unable to log food. Please try again."
            )
        }
    }
}