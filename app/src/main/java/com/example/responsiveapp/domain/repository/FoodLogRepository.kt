package com.example.responsiveapp.domain.repository

import com.example.responsiveapp.domain.model.foodlog.FoodLog
import kotlinx.coroutines.flow.Flow

interface FoodLogRepository {

    suspend fun logFood(foodLog: FoodLog)

    fun observeFoodLogsForDate(date: Long): Flow<List<FoodLog>>

    fun observeLoggedDates(): Flow<List<Long>>

    suspend fun syncPending()

    suspend fun fetchAndCacheAll()
}