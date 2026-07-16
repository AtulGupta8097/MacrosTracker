package com.example.responsiveapp.data.remote.dto.firebase

data class MyMealDto(

    val id: String = "",

    val name: String = "",

    val ingredientsJson: String = "",

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

    val createdAt: Long = 0L,

    val updatedAt: Long = 0L
)