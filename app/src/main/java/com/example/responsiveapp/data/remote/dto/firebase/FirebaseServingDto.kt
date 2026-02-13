package com.example.responsiveapp.data.remote.dto.firebase

data class FirebaseServingDto(
    val id: String = "",
    val description: String = "",
    val amount: Float = 0f,
    val unit: String = "GRAM",
    val calories: Float = 0f,
    val protein: Float = 0f,
    val carbs: Float = 0f,
    val fat: Float = 0f,
    val fiber: Float = 0f,
    val sugar: Float = 0f,
    val sodium: Float = 0f,
    val cholesterol: Float = 0f,
    val saturatedFat: Float = 0f,
    val transFat: Float = 0f,
    val isDefault: Boolean = false
)