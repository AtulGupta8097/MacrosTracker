package com.macrivo.app.domain.repository

import com.macrivo.app.domain.model.foodlog.FoodLog
import kotlinx.coroutines.flow.Flow

interface FoodLogRepository {

    suspend fun logFood(foodLog: FoodLog)

    fun observeFoodLogsForDate(date: Long): Flow<List<FoodLog>>

    fun observeLoggedDates(): Flow<List<Long>>

    fun observeMealCountsBetweenDates(startDate: Long, endDate: Long): Flow<Map<Long, Int>>

    suspend fun syncPending()

    suspend fun fetchAndCacheAll()
}