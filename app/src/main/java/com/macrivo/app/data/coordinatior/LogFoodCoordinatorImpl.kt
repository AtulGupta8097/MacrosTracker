package com.macrivo.app.data.coordinatior

import androidx.room.withTransaction
import com.macrivo.app.core.utils.todayStartOfDay
import com.macrivo.app.data.local.database.MacrosTrackerDatabase
import com.macrivo.app.domain.model.DailySummary
import com.macrivo.app.domain.model.foodlog.FoodLog
import com.macrivo.app.domain.model.NutritionProgress
import com.macrivo.app.domain.model.foodlog.LogFoodRequest
import com.macrivo.app.domain.model.plus
import com.macrivo.app.domain.repository.DailySummaryRepository
import com.macrivo.app.domain.repository.FoodLogRepository
import com.macrivo.app.domain.repository.MacroTargetRepository
import com.macrivo.app.sync.SyncScheduler
import com.macrivo.app.sync.SyncType
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
