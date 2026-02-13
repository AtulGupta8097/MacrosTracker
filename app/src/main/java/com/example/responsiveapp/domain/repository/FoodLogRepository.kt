package com.example.responsiveapp.domain.repository

import com.example.responsiveapp.domain.model.FoodLog

interface FoodLogRepository {

    suspend fun logFood(foodLog: FoodLog): Result<FoodLog>

    suspend fun syncPendingLogs(userId: String): Result<Int>
}