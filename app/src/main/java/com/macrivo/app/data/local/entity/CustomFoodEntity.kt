package com.macrivo.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.macrivo.app.domain.model.NutritionInfo
import com.macrivo.app.domain.model.SyncStatus

@Entity(tableName = "custom_foods")
data class CustomFoodEntity(

    @PrimaryKey
    val id: String,

    // Basic Info
    val name: String,
    val description: String,
    val servingSize: String,
    val servingsPerContainer: Float,

    // Nutrition
    val nutrition: NutritionInfo,
    val createdAt: Long,
    val updatedAt: Long,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val retryCount: Int = 0,
    val lastSyncAttempt: Long? = null,
)