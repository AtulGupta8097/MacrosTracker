package com.example.responsiveapp.domain.use_case.foodlog_usecase

import com.example.responsiveapp.domain.model.FoodLog
import com.example.responsiveapp.domain.model.LogFoodRequest
import com.example.responsiveapp.domain.repository.FoodLogRepository
import com.example.responsiveapp.domain.repository.FoodRepository
import javax.inject.Inject

class LogFoodUseCase @Inject constructor(
    private val foodLogRepository: FoodLogRepository,
    private val foodRepository: FoodRepository
) {

    suspend operator fun invoke(
        request: LogFoodRequest
    ): Result<FoodLog> {

        if (request.quantity <= 0) {
            return Result.failure(
                IllegalArgumentException("Quantity must be greater than 0")
            )
        }

        if (request.userId.isBlank()) {
            return Result.failure(
                IllegalArgumentException("User ID is required")
            )
        }

        val food = foodRepository.getFoodById(request.foodId)
            .getOrElse { return Result.failure(it) }

        val serving = food.servings.find { it.id == request.servingId }
            ?: return Result.failure(
                IllegalArgumentException("Invalid serving ID")
            )

        val nutrition =
            serving.nutrition.calculateForQuantity(request.quantity)

        val foodLog = FoodLog(
            id = generateLogId(),
            userId = request.userId,
            foodId = food.id,
            foodName = food.name,
            brand = food.brand,
            servingId = serving.id,
            servingDescription = serving.description,
            quantity = request.quantity,
            nutrition = nutrition,
            date = request.date,
            logMethod = request.logMethod,
            notes = request.notes,
            imageUrl = request.imageUrl,
            createdAt = System.currentTimeMillis(),
            isSynced = false
        )

        return foodLogRepository.logFood(foodLog)
    }

    private fun generateLogId(): String {
        return "log_${System.currentTimeMillis()}_${(0..9999).random()}"
    }
}
