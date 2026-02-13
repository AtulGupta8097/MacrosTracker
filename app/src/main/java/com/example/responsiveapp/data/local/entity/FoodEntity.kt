package com.example.responsiveapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.responsiveapp.data.local.converter.Converters

@Entity(tableName = "foods")
data class FoodEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val brand: String?,
    @TypeConverters(Converters::class)
    val servings: List<ServingEntity>,
    val imageUrl: String?,
    val barcode: String?,
    val isVerified: Boolean,
    val createdAt: Long,
    val searchableName: String,
    val source: String,  // firebase, fatsecret, custom
    val cachedAt: Long = System.currentTimeMillis()
)
data class ServingEntity(
    val id: String,
    val description: String,
    val amount: Float,
    val unit: String,
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
    val isDefault: Boolean
)