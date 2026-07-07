package com.example.responsiveapp.domain.use_case.dailysummary

import com.example.responsiveapp.domain.model.FoodLog
import com.example.responsiveapp.domain.model.plus
import com.example.responsiveapp.domain.repository.DailySummaryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateDailySummaryUseCase @Inject constructor(
    private val dailySummaryRepository: DailySummaryRepository,
    private val getOrCreate: GetOrCreateDailySummaryUseCase
) {

    suspend operator fun invoke(foodLog: FoodLog): Result<Unit> {
        return try {
            val summary = getOrCreate().getOrThrow()

            val updatedSummary = summary.copy(
                consumed = summary.consumed + foodLog.nutrition,
                updatedAt = System.currentTimeMillis()
            )

            dailySummaryRepository.update(updatedSummary)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}