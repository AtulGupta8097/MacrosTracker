package com.macrivo.app.domain.model.macros

import com.macrivo.app.domain.model.NutritionTargets
import com.macrivo.app.domain.model.SyncStatus

data class MacroTarget(
    val id: String,
    val targets: NutritionTargets,
    val bmr: Int,
    val tdee: Int,
    val bmi: Float,
    val createdAt: Long,
    val updatedAt: Long,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val retryCount: Int = 0,
    val lastSyncAttempt: Long? = null
)