package com.example.responsiveapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_items")
data class FoodItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val brand: String?,
    val foodType: String,
    // Store MacroSummary as flat columns — it's just one row of numbers
    val servingLabel: String?,      // "100g", "14 pieces"
    val calories: Float?,
    val fat: Float?,
    val carbs: Float?,
    val protein: Float?,
    val cachedAt: Long = System.currentTimeMillis(),
)