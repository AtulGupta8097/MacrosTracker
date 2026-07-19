package com.example.responsiveapp.domain.model.foodlog

import com.example.responsiveapp.domain.model.MealIngredient
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.model.SyncStatus

data class FoodLog(
    val id: String,
    val date: Long = 0L,
    val foodName: String = "",
    val servingDescription: String = "",
    val quantity: Float = 1f,
    val nutrition: NutritionInfo = NutritionInfo(),
    val ingredients: List<MealIngredient> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val retryCount: Int = 0,
    val lastSyncAttempt: Long? = null
)