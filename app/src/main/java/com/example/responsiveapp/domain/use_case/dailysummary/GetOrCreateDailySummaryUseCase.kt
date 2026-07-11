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

    suspend operator fun invoke(): DailySummary {

        val today = todayStartOfDay()

        dailySummaryRepository.getForDate(today)?.let {
            return it
        }

        val macroTarget = macroTargetRepository.getCurrentTarget()
            ?: throw IllegalStateException(
                "No MacroTarget found. Complete user setup before logging food."
            )

        val now = System.currentTimeMillis()

        val summary = DailySummary(
            date = today,
            target = macroTarget.targets,
            consumed = NutritionProgress(),
            createdAt = now,
            updatedAt = now
        )

        dailySummaryRepository.insert(summary)

        return summary
    }
}