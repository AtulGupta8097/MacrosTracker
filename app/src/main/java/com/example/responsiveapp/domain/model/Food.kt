package com.example.responsiveapp.domain.model

data class Food(
    val id: String = "",
    val name: String = "",
    val brand: String? = null,
    val category: FoodCategory = FoodCategory.OTHER,
    val servings: List<Serving> = emptyList(),
    val imageUrl: String? = null,
    val barcode: String? = null,
    val isVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val searchableName: String = name.lowercase()
)