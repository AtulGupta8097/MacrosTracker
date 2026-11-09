package com.macrivo.app.data.coordinatior

import com.macrivo.app.domain.model.foodlog.LogFoodRequest

interface LogFoodCoordinator {

    suspend fun logFood(foodLog: LogFoodRequest)

    // CHANGED: changes a log's quantity and re-scales that day's summary, atomically. Today only.
    suspend fun updateFoodLogQuantity(id: String, quantity: Float)

    // CHANGED: removes a log and takes its nutrition back out of that day's summary, atomically.
    suspend fun deleteFoodLog(id: String)
}