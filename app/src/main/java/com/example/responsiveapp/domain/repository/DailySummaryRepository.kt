package com.example.responsiveapp.domain.repository

import com.example.responsiveapp.domain.model.DailySummary
import com.example.responsiveapp.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow

interface DailySummaryRepository {

    suspend fun insert(summary: DailySummary)

    suspend fun update(summary: DailySummary)

    fun observeForDate(date: Long): Flow<DailySummary?>
    fun observeSummariesBetweenDates(startDate: Long, endDate: Long): Flow<List<DailySummary>>

    suspend fun getForDate(date: Long): DailySummary?

    suspend fun syncPending()

    suspend fun fetchAndCacheAll()
}