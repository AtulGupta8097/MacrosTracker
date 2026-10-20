package com.example.responsiveapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.responsiveapp.data.local.converter.ServingConverter
import com.example.responsiveapp.domain.model.SyncStatus

@Entity(tableName = "saved_foods")
data class SavedFoodEntity(
    @PrimaryKey val id: String,
    val name: String,
    val brand: String?,
    val foodType: String,
    val imageUrl: String?,
    val barcode: String?,
    val isVerified: Boolean,
    val servings: List<ServingEntity>,
    val savedAt: Long,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val retryCount: Int = 0,
    val lastSyncAttempt: Long? = null,
)
