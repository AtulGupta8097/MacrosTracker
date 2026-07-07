package com.example.responsiveapp.domain.repository

import com.example.responsiveapp.domain.model.DailySummary
import com.example.responsiveapp.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow

interface DailySummaryRepository {

    suspend fun insert(summary: DailySummary)

    suspend fun update(summary: DailySummary)

    fun observeForDate(startOfDay: Long): Flow<DailySummary?>

    suspend fun getForDate(startOfDay: Long): DailySummary?

    suspend fun getPendingSummaries(): List<DailySummary>

    suspend fun updateSyncStatus(date: Long, status: SyncStatus)

    suspend fun updateRetryInfo(date: Long, retryCount: Int, lastSyncAttempt: Long)
}