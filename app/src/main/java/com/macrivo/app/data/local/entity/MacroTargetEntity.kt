package com.macrivo.app.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.macrivo.app.domain.model.NutritionTargets
import com.macrivo.app.domain.model.SyncStatus

@Entity(tableName = "macro_targets")
data class MacroTargetEntity(

    @PrimaryKey
    val id: String,

    @Embedded(prefix = "target_")
    val targets: NutritionTargets,

    val bmr: Int,

    val tdee: Int,

    val bmi: Float,

    val createdAt: Long,

    val syncStatus: SyncStatus = SyncStatus.PENDING,

    val retryCount: Int = 0,

    val lastSyncAttempt: Long? = null,
    val updatedAt: Long

)