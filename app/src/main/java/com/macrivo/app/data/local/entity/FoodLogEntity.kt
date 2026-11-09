package com.macrivo.app.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.macrivo.app.domain.model.NutritionInfo
import com.macrivo.app.domain.model.SyncStatus

@Entity(tableName = "food_logs")
data class FoodLogEntity(

    @PrimaryKey
    val id: String,

    val date: Long,

    val foodName: String,

    val servingDescription: String,

    val quantity: Float,

    @Embedded(prefix = "nutrition_")
    val nutrition: NutritionInfo = NutritionInfo(),

    val ingredientsJson: String? = null,

    val createdAt: Long,

    val updatedAt: Long,

    val syncStatus: SyncStatus = SyncStatus.PENDING,

    val retryCount: Int = 0,

    val lastSyncAttempt: Long? = null
)