package com.example.responsiveapp.domain.model.macros

import com.example.responsiveapp.domain.model.NutritionTargets
import com.example.responsiveapp.domain.model.SyncStatus

data class MacroTarget(
    val id: String,
    val targets: NutritionTargets,
    val bmr: Int,
    val tdee: Int,
    val createdAt: Long,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val retryCount: Int = 0,
    val lastSyncAttempt: Long? = null
)