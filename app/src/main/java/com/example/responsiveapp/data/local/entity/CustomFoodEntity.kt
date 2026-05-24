package com.example.responsiveapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.responsiveapp.domain.model.NutritionInfo

@Entity(tableName = "custom_foods")
data class CustomFoodEntity(

    @PrimaryKey
    val id: String,

    // Basic Info
    val name: String,
    val description: String,
    val servingSize: String,
    val servingsPerContainer: Float,

    // Nutrition
    val nutrition: NutritionInfo,
    val createdAt: Long
)