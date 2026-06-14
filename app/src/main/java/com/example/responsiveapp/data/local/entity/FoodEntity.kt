package com.example.responsiveapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_items")
data class FoodItemEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val brand: String?,
    val foodType: String,
    val servingLabel: String?,
    val calories: Float?,
    val fat: Float?,
    val carbs: Float?,
    val protein: Float?,
    val cachedAt: Long = System.currentTimeMillis(),
)