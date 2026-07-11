package com.example.responsiveapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.responsiveapp.domain.model.SyncStatus

@Entity(tableName = "my_meals")
data class MyMealsEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val ingredientsJson: String,   // JSON list of MealIngredient
    val calories: Float = 0f,
    val protein: Float = 0f,
    val carbs: Float = 0f,
    val fat: Float = 0f,
    val fiber: Float = 0f,
    val sugar: Float = 0f,
    val sodium: Float = 0f,
    val cholesterol: Float = 0f,
    val saturatedFat: Float = 0f,
    val polyunsaturatedFat: Float = 0f,
    val monounsaturatedFat: Float = 0f,
    val transFat: Float = 0f,
    val potassium: Float = 0f,
    val addedSugars: Float = 0f,
    val vitaminA: Float = 0f,
    val vitaminC: Float = 0f,
    val vitaminD: Float = 0f,
    val calcium: Float = 0f,
    val iron: Float = 0f,
    val createdAt: Long,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val retryCount: Int = 0,
    val lastSyncAttempt: Long? = null,
)
