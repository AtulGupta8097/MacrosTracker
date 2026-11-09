package com.macrivo.app.domain.use_case.foodlog

import com.macrivo.app.core.utils.Resource
import com.macrivo.app.data.coordinatior.LogFoodCoordinator
import javax.inject.Inject

// CHANGED: new file.
class UpdateFoodLogQuantityUseCase @Inject constructor(
    private val coordinator: LogFoodCoordinator
) {

    suspend operator fun invoke(id: String, quantity: Float): Resource<Unit> =
        try {
            coordinator.updateFoodLogQuantity(id, quantity)
            Resource.Success(Unit)
        } catch (e: IllegalStateException) {
            Resource.Error(e.message ?: "Unable to update this food.")
        } catch (e: IllegalArgumentException) {
            Resource.Error(e.message ?: "Enter a valid quantity.")
        } catch (e: Exception) {
            Resource.Error("Unable to update this food. Please try again.")
        }
}
