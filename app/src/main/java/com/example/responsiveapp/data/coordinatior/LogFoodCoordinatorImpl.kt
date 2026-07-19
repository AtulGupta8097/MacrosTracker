package com.example.responsiveapp.data.coordinatior

import androidx.room.withTransaction
import com.example.responsiveapp.core.utils.todayStartOfDay
import com.example.responsiveapp.data.local.database.MacrosTrackerDatabase
import com.example.responsiveapp.domain.model.DailySummary
import com.example.responsiveapp.domain.model.foodlog.FoodLog
import com.example.responsiveapp.domain.model.NutritionProgress
import com.example.responsiveapp.domain.model.foodlog.LogFoodRequest
import com.example.responsiveapp.domain.model.plus
import com.example.responsiveapp.domain.repository.DailySummaryRepository
import com.example.responsiveapp.domain.repository.FoodLogRepository
import com.example.responsiveapp.domain.repository.MacroTargetRepository
import com.example.responsiveapp.sync.SyncScheduler
import com.example.responsiveapp.sync.SyncType
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogFoodCoordinatorImpl @Inject constructor(
    private val database: MacrosTrackerDatabase,
    private val foodLogRepository: FoodLogRepository,
    private val dailySummaryRepository: DailySummaryRepository,
    private val macroTargetRepository: MacroTargetRepository,
    private val scheduler: SyncScheduler,
) : LogFoodCoordinator {

    override suspend fun logFood(request: LogFoodRequest) {

        val now = System.currentTimeMillis()

        val foodLog = FoodLog(

            id = UUID.randomUUID().toString(),

            date = todayStartOfDay(),

            foodName = request.foodName,

            servingDescription = request.servingDescription,

            quantity = request.quantity,

            nutrition = request.nutrition,

            ingredients = request.ingredients,

            createdAt = now,

            updatedAt = now

        )

        database.withTransaction {

            foodLogRepository.logFood(foodLog)

            val existingSummary = dailySummaryRepository.getForDate(foodLog.date)

            if (existingSummary == null) {

                val newSummary = createSummary(
                    date = foodLog.date,
                    foodLog = foodLog,
                    now = now,
                )

                dailySummaryRepository.insert(newSummary)

            } else {

                val updatedSummary = existingSummary.copy(
                    consumed = existingSummary.consumed + foodLog.nutrition,
                    updatedAt = now,
                )

                dailySummaryRepository.update(updatedSummary)
            }
        }

        scheduler.schedule(SyncType.FOOD_LOG)
        scheduler.schedule(SyncType.DAILY_SUMMARY)
    }

    private suspend fun createSummary(
        date: Long,
        foodLog: FoodLog,
        now: Long,
    ): DailySummary {

        val target = macroTargetRepository.getCurrentTarget()
            ?: throw IllegalStateException(
                "No MacroTarget found. Complete user setup before logging food."
            )

        return DailySummary(
            date = date,
            target = target.targets,
            consumed = NutritionProgress() + foodLog.nutrition,
            createdAt = now,
            updatedAt = now,
        )
    }
}
