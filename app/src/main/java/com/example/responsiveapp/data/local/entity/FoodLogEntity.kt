package com.example.responsiveapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_logs")
data class FoodLogEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val foodId: String,
    val foodName: String,
    val brand: String?,
    val servingId: String,
    val servingDescription: String,
    val quantity: Float,
    val calories: Float,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val fiber: Float,
    val sugar: Float,
    val sodium: Float,
    val cholesterol: Float,
    val saturatedFat: Float,
    val transFat: Float,
    val date: Long,
    val logMethod: String,
    val notes: String?,
    val imageUrl: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val syncStatus: String  // PENDING, SYNCED, FAILED
)