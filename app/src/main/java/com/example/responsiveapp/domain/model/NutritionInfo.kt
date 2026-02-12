package com.example.responsiveapp.domain.model

data class NutritionInfo(
    val calories: Float = 0f,
    val protein: Float = 0f,      // grams
    val carbs: Float = 0f,        // grams
    val fat: Float = 0f,          // grams
    val fiber: Float = 0f,        // grams
    val sugar: Float = 0f,        // grams
    val sodium: Float = 0f,       // mg
    val cholesterol: Float = 0f,  // mg
    val saturatedFat: Float = 0f, // grams
    val transFat: Float = 0f      // grams
) {

    fun calculateForQuantity(quantity: Float): NutritionInfo {
        return NutritionInfo(
            calories = calories * quantity,
            protein = protein * quantity,
            carbs = carbs * quantity,
            fat = fat * quantity,
            fiber = fiber * quantity,
            sugar = sugar * quantity,
            sodium = sodium * quantity,
            cholesterol = cholesterol * quantity,
            saturatedFat = saturatedFat * quantity,
            transFat = transFat * quantity
        )
    }
    
    /**
     * Add nutrition from multiple foods
     */
    operator fun plus(other: NutritionInfo): NutritionInfo {
        return NutritionInfo(
            calories = calories + other.calories,
            protein = protein + other.protein,
            carbs = carbs + other.carbs,
            fat = fat + other.fat,
            fiber = fiber + other.fiber,
            sugar = sugar + other.sugar,
            sodium = sodium + other.sodium,
            cholesterol = cholesterol + other.cholesterol,
            saturatedFat = saturatedFat + other.saturatedFat,
            transFat = transFat + other.transFat
        )
    }
}