package com.example.responsiveapp.domain.repository

import com.example.responsiveapp.domain.model.weight.WeightLog
import kotlinx.coroutines.flow.Flow

interface WeightRepository {

    suspend fun saveWeight(weightLog: WeightLog)

    suspend fun updateWeight(weightLog: WeightLog)

    fun observeLatestWeight(): Flow<WeightLog?>

    fun observeWeightHistory(): Flow<List<WeightLog>>

    fun observeWeightBetweenDates(startDate: Long, endDate: Long): Flow<List<WeightLog>>

    suspend fun syncPending()

    suspend fun fetchAndCacheAll()
}
