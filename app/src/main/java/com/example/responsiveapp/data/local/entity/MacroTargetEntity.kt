package com.example.responsiveapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.responsiveapp.domain.model.NutritionTargets
import com.example.responsiveapp.domain.model.SyncStatus

@Entity(tableName = "macro_targets")
data class MacroTargetEntity(

    @PrimaryKey
    val id: String,

    @Embedded(prefix = "target_")
    val targets: NutritionTargets,

    val bmr: Int,

    val tdee: Int,

    val createdAt: Long,

    val syncStatus: SyncStatus = SyncStatus.PENDING,

    val retryCount: Int = 0,

    val lastSyncAttempt: Long? = null,
    val updatedAt: Long

)