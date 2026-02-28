package com.example.responsiveapp.data.mapper

import com.example.responsiveapp.data.local.entity.FoodLogEntity
import com.example.responsiveapp.domain.model.FoodLog
import com.example.responsiveapp.domain.model.MealIngredient
import com.example.responsiveapp.domain.model.NutritionInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun FoodLogEntity.toDomain(): FoodLog {
    val ingredients: List<MealIngredient> = if (ingredientsJson != null) {
        val type = object : TypeToken<List<MealIngredient>>() {}.type
        Gson().fromJson(ingredientsJson, type)
    } else emptyList()

    return FoodLog(
        id = id,
        userId = userId,
        date = date,
        logSource = enumValueOf(logSource),
        foodId = foodId,
        brand = brand,
        servingId = servingId,
        foodName = foodName,
        servingDescription = servingDescription,
        quantity = quantity,
        nutrition = NutritionInfo(
            calories = calories,
            protein = protein,
            carbs = carbs,
            fat = fat,
            fiber = fiber,
            sugar = sugar,
            sodium = sodium,
            cholesterol = cholesterol,
            saturatedFat = saturatedFat,
            transFat = transFat
        ),
        ingredients = ingredients,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isSynced = syncStatus == "SYNCED"
    )
}
fun FoodLog.toEntity(): FoodLogEntity = FoodLogEntity(
    id = id,
    userId = userId,
    date = date,
    logSource = logSource.name,
    foodId = foodId,
    brand = brand,
    servingId = servingId,
    foodName = foodName,
    servingDescription = servingDescription,
    quantity = quantity,
    calories = nutrition.calories,
    protein = nutrition.protein,
    carbs = nutrition.carbs,
    fat = nutrition.fat,
    fiber = nutrition.fiber,
    sugar = nutrition.sugar,
    sodium = nutrition.sodium,
    cholesterol = nutrition.cholesterol,
    saturatedFat = nutrition.saturatedFat,
    transFat = nutrition.transFat,
    ingredientsJson = if (ingredients.isNotEmpty()) Gson().toJson(ingredients) else null,
    notes = notes,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncStatus = if (isSynced) "SYNCED" else "PENDING"
)