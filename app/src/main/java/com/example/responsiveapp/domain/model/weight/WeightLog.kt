package com.example.responsiveapp.domain.model.weight

import com.example.responsiveapp.domain.model.SyncStatus

data class WeightLog(
    val id: String,
    val weight: Float,
    val date: Long,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val retryCount: Int = 0,
    val lastSyncAttempt: Long? = null,
)
