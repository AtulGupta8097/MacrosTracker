package com.example.responsiveapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.responsiveapp.domain.model.SyncStatus

@Entity(tableName = "weight_logs")
data class WeightLogEntity(

    @PrimaryKey
    val id: String,

    val weight: Float,
    val date: Long,

    val createdAt: Long,
    val updatedAt: Long,

    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val retryCount: Int = 0,
    val lastSyncAttempt: Long? = null,
)
