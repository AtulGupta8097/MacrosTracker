package com.example.responsiveapp.data.coordinatior

import com.example.responsiveapp.domain.model.FoodLog

interface LogFoodCoordinator {

    suspend fun logFood(foodLog: FoodLog)
}