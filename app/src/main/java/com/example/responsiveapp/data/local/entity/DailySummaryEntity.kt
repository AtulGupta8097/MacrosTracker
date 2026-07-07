package com.example.responsiveapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.responsiveapp.domain.model.NutritionProgress
import com.example.responsiveapp.domain.model.NutritionTargets
import com.example.responsiveapp.domain.model.SyncStatus

@Entity(tableName = "daily_summaries")
data class DailySummaryEntity(

    @PrimaryKey
    val date: Long,

    @Embedded(prefix = "target_")
    val target: NutritionTargets,

    @Embedded(prefix = "consumed_")
    val consumed: NutritionProgress,

    val createdAt: Long,

    val updatedAt: Long,

    val syncStatus: SyncStatus = SyncStatus.PENDING,

    val retryCount: Int = 0,

    val lastSyncAttempt: Long? = null
)