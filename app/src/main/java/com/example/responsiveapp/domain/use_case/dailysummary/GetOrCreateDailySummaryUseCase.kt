package com.example.responsiveapp.domain.use_case.dailysummary

import com.example.responsiveapp.core.utils.todayStartOfDay
import com.example.responsiveapp.domain.model.DailySummary
import com.example.responsiveapp.domain.model.NutritionProgress
import com.example.responsiveapp.domain.repository.DailySummaryRepository
import com.example.responsiveapp.domain.repository.MacroTargetRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetOrCreateDailySummaryUseCase @Inject constructor(
    private val dailySummaryRepository: DailySummaryRepository,
    private val macroTargetRepository: MacroTargetRepository
) {

    suspend operator fun invoke(): Result<DailySummary> {
        return try {
            val today = todayStartOfDay()

            dailySummaryRepository.getForDate(today)?.let {
                return Result.success(it)
            }

            val macroTarget = macroTargetRepository.getCurrentTarget()
                ?: return Result.failure(
                    IllegalStateException(
                        "No MacroTarget found. Complete user setup before logging food."
                    )
                )

            val now = System.currentTimeMillis()

            val newSummary = DailySummary(
                date = today,
                target = macroTarget.targets,
                consumed = NutritionProgress(),
                createdAt = now,
                updatedAt = now
            )

            dailySummaryRepository.insert(newSummary)

            Result.success(
                dailySummaryRepository.getForDate(today) ?: newSummary
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}