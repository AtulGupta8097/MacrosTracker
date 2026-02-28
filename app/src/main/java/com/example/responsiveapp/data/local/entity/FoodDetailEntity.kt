package com.example.responsiveapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.responsiveapp.data.local.converter.Converters

@Entity(tableName = "food_details")
data class FoodDetailEntity(
    @PrimaryKey val id: String,
    val name: String,
    val brand: String?,
    val foodType: String,
    val imageUrl: String?,
    val barcode: String?,
    val isVerified: Boolean,
    @TypeConverters(Converters::class)
    val servings: List<ServingEntity>,
    val cachedAt: Long = System.currentTimeMillis(),
)

data class ServingEntity(
    val id: String,
    val description: String,
    val metricAmount: Float,
    val metricUnit: String,
    val numberOfUnits: Float,
    val measurementDescription: String,
    val calories: Float,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val fiber: Float,
    val sugar: Float,
    val sodium: Float,
    val cholesterol: Float,
    val saturatedFat: Float,
    val polyunsaturatedFat: Float,
    val monounsaturatedFat: Float,
    val transFat: Float,
    val potassium: Float,
    val addedSugars: Float,
    val vitaminD: Float,
    val calcium: Float,
    val iron: Float,
    val isDefault: Boolean,
)