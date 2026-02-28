package com.example.responsiveapp.domain.model

data class FoodDetail(
    val id: String,
    val name: String,
    val brand: String? = null,
    val foodType: String,
    val servings: List<Serving>,
    val imageUrl: String? = null,
    val barcode: String? = null,
    val isVerified: Boolean = true,
    val cachedAt: Long = System.currentTimeMillis(),
) {
    val defaultServing: Serving?
        get() = servings.firstOrNull { it.isDefault } ?: servings.firstOrNull()
}


