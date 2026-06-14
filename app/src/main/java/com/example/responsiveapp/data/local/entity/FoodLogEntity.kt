package com.example.responsiveapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.model.SyncStatus

@Entity(tableName = "food_logs")
data class FoodLogEntity(

    @PrimaryKey
    val id: String,

    val userId: String,

    val date: Long,

    val foodName: String,

    val servingDescription: String,

    val quantity: Float,

    @Embedded(prefix = "nutrition_")
    val nutrition: NutritionInfo = NutritionInfo(),

    val ingredientsJson: String? = null,

    val createdAt: Long,

    val updatedAt: Long,

    // Sync metadata
    val syncStatus: SyncStatus = SyncStatus.PENDING,

    val lastSyncAttempt: Long? = null,

    val retryCount: Int = 0
)