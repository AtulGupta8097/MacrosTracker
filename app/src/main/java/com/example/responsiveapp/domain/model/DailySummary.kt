package com.example.responsiveapp.domain.model

data class DailySummary(
    val id: String,
    val date: Long,
    val target: NutritionTargets,
    val consumed: NutritionInfo,
    val createdAt: Long,
    val updatedAt: Long,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val retryCount: Int = 0,
    val lastSyncAttempt: Long? = null
)