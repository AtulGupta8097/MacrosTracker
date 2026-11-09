package com.macrivo.app.domain.use_case.foodlog

import com.macrivo.app.core.utils.Resource
import com.macrivo.app.data.coordinatior.LogFoodCoordinator
import javax.inject.Inject

// CHANGED: new file.
class DeleteFoodLogUseCase @Inject constructor(
    private val coordinator: LogFoodCoordinator
) {

    suspend operator fun invoke(id: String): Resource<Unit> =
        try {
            coordinator.deleteFoodLog(id)
            Resource.Success(Unit)
        } catch (e: IllegalStateException) {
            Resource.Error(e.message ?: "Unable to remove this food.")
        } catch (e: Exception) {
            Resource.Error("Unable to remove this food. Please try again.")
        }
}
