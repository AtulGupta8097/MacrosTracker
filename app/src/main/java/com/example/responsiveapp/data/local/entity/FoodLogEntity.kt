package com.example.responsiveapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_logs")
data class FoodLogEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val date: Long,
    val logSource: String,  // "DATABASE", "CUSTOM_MEAL", "MANUAL_ENTRY", etc.
    // ── Nullable: only set for DATABASE/BARCODE_SCAN ──────────────────────────
    val foodId: String? = null,
    val brand: String? = null,
    val servingId: String? = null,
    // ── Always present ────────────────────────────────────────────────────────
    val foodName: String,
    val servingDescription: String,
    val quantity: Float,
    // ── Nutrition (flattened from NutritionInfo) ──────────────────────────────
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
    val ingredientsJson: String? = null,
    val notes: String? = null,
    val imageUrl: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val syncStatus: String
)