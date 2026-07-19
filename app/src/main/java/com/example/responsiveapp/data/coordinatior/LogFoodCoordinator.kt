package com.example.responsiveapp.data.coordinatior

import com.example.responsiveapp.domain.model.foodlog.LogFoodRequest

interface LogFoodCoordinator {

    suspend fun logFood(foodLog: LogFoodRequest)
}