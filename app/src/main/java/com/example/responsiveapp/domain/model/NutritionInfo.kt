package com.example.responsiveapp.domain.model

data class NutritionInfo(
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
    val vitaminD: Float = 0f,
    val calcium: Float = 0f,
    val iron: Float = 0f,
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