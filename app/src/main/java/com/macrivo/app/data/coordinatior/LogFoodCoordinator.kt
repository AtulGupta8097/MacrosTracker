package com.macrivo.app.data.coordinatior

import com.macrivo.app.domain.model.foodlog.LogFoodRequest

interface LogFoodCoordinator {

    suspend fun logFood(foodLog: LogFoodRequest)
}