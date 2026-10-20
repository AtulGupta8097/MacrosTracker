package com.example.responsiveapp.domain.model

import androidx.compose.runtime.Immutable

@Immutable
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


    fun FoodDetail.toFoodItem(): FoodItem {
        val serving = defaultServing
        return FoodItem(
            id = id,
            name = name,
            brand = brand,
            foodType = foodType,
            macroSummary = MacroSummary(
                servingLabel = serving?.description.orEmpty(),
                calories = serving?.nutrition?.calories ?: 0f,
                fat = serving?.nutrition?.fat ?: 0f,
                carbs = serving?.nutrition?.carbs ?: 0f,
                protein = serving?.nutrition?.protein ?: 0f,
            ),
        )
    }
}


