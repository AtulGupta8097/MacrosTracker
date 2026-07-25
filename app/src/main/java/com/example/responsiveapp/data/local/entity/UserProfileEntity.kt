package com.example.responsiveapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.responsiveapp.domain.model.SyncStatus

@Entity(tableName = "user_profile")
data class UserProfileEntity(

    @PrimaryKey
    val id: String,

    val name: String,
    val gender: String,
    val age: Int,
    val height: Float,
    val weight: Float,
    val targetWeight: Float,
    val activityLevel: String,
    val goal: String,
    val updatedAt: Long,

    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val retryCount: Int = 0,
    val lastSyncAttempt: Long? = null,
)
