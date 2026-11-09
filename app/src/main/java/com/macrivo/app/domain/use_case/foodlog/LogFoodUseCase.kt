package com.macrivo.app.domain.use_case.foodlog

import com.macrivo.app.core.utils.Resource
import com.macrivo.app.data.coordinatior.LogFoodCoordinator
import com.macrivo.app.domain.model.foodlog.LogFoodRequest
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