package com.example.responsiveapp.data.mapper

import com.example.responsiveapp.data.local.entity.FoodEntity
import com.example.responsiveapp.data.local.entity.FoodLogEntity
import com.example.responsiveapp.data.local.entity.ServingEntity
import com.example.responsiveapp.data.remote.dto.fatsecret.detail.FoodDetailDto
import com.example.responsiveapp.data.remote.dto.fatsecret.detail.ServingDto
import com.example.responsiveapp.data.remote.dto.fatsecret.search.FoodItemDto
import com.example.responsiveapp.domain.model.Food
import com.example.responsiveapp.domain.model.FoodLog
import com.example.responsiveapp.domain.model.MealIngredient
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.domain.model.ServingUnit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun FoodItemDto.toDomain(): Food {

    val parsedServing = parseServingFromDescription(foodDescription)

    return Food(
        id = foodId,
        name = foodName,
        brand = brandName,
        servings = parsedServing?.let { listOf(it) } ?: emptyList(),
        imageUrl = null,
        barcode = null,
        isVerified = true,
        createdAt = System.currentTimeMillis(),
        searchableName = foodName.lowercase()
    )
}


fun FoodDetailDto.toDomain(): Food {
    return Food(
        id = foodId,
        name = foodName,
        brand = brandName,
        servings = servings.serving.map { it.toDomain() },
        imageUrl = null,
        barcode = null,
        isVerified = true,
        createdAt = System.currentTimeMillis(),
        searchableName = foodName.lowercase()
    )
}

fun ServingDto.toDomain(): Serving {
    return Serving(
        id = servingId,
        description = servingDescription,
        amount = metricServingAmount?.toFloatOrNull() ?: numberOfUnits?.toFloatOrNull() ?: 100f,
        unit = parseServingUnit(metricServingUnit ?: measurementDescription ?: "g"),
        nutrition = NutritionInfo(
            calories = calories?.toFloatOrNull() ?: 0f,
            protein = protein?.toFloatOrNull() ?: 0f,
            carbs = carbohydrate?.toFloatOrNull() ?: 0f,
            fat = fat?.toFloatOrNull() ?: 0f,
            fiber = fiber?.toFloatOrNull() ?: 0f,
            sugar = sugar?.toFloatOrNull() ?: 0f,
            sodium = sodium?.toFloatOrNull() ?: 0f,
            cholesterol = cholesterol?.toFloatOrNull() ?: 0f,
            saturatedFat = saturatedFat?.toFloatOrNull() ?: 0f,
            transFat = 0f  // FatSecret doesn't provide trans fat
        ),
        isDefault = false
    )
}

/**
 * ========== ROOM ENTITY → DOMAIN ==========
 */

fun FoodEntity.toDomain(): Food {
    return Food(
        id = id,
        name = name,
        brand = brand,
        servings = servings.map { it.toDomain() },
        imageUrl = imageUrl,
        barcode = barcode,
        isVerified = isVerified,
        createdAt = createdAt,
        searchableName = searchableName
    )
}

fun ServingEntity.toDomain(): Serving {
    return Serving(
        id = id,
        description = description,
        amount = amount,
        unit = ServingUnit.valueOf(unit),
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
        isDefault = isDefault
    )
}


fun FoodLog.toEntity(): FoodLogEntity {
    val gson = Gson()
    val ingredientsJson = if (ingredients.isNotEmpty()) {
        gson.toJson(ingredients)
    } else null

    return FoodLogEntity(
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
        ingredientsJson = ingredientsJson,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt,
        syncStatus = if (isSynced) "SYNCED" else "PENDING"
    )
}

// ═══════════════════════════════════════════════════════════════════════════
// Entity → Domain
// ═══════════════════════════════════════════════════════════════════════════

fun FoodLogEntity.toDomain(): FoodLog {
    val gson = Gson()
    val ingredients = if (ingredientsJson != null) {
        val type = object : TypeToken<List<MealIngredient>>() {}.type
        gson.fromJson<List<MealIngredient>>(ingredientsJson, type)
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

/**
 * ========== DOMAIN → ROOM ENTITY ==========
 */

fun Food.toEntity(source: String = "fatsecret"): FoodEntity {
    return FoodEntity(
        id = id,
        name = name,
        brand = brand,
        servings = servings.map { it.toEntity() },
        imageUrl = imageUrl,
        barcode = barcode,
        isVerified = isVerified,
        createdAt = createdAt,
        searchableName = searchableName,
        source = source
    )
}

fun Serving.toEntity(): ServingEntity {
    return ServingEntity(
        id = id,
        description = description,
        amount = amount,
        unit = unit.name,
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
        isDefault = isDefault
    )
}

/**
 * ========== HELPER FUNCTIONS ==========
 */

private fun parseServingUnit(unitString: String): ServingUnit {
    return when (unitString.lowercase()) {
        "g", "gram", "grams" -> ServingUnit.GRAM
        "kg", "kilogram", "kilograms" -> ServingUnit.KILOGRAM
        "oz", "ounce", "ounces" -> ServingUnit.OUNCE
        "lb", "pound", "pounds" -> ServingUnit.POUND
        "ml", "milliliter", "milliliters" -> ServingUnit.MILLILITER
        "l", "liter", "liters" -> ServingUnit.LITER
        "cup", "cups" -> ServingUnit.CUP
        "tbsp", "tablespoon", "tablespoons" -> ServingUnit.TABLESPOON
        "tsp", "teaspoon", "teaspoons" -> ServingUnit.TEASPOON
        "piece", "pieces" -> ServingUnit.PIECE
        "slice", "slices" -> ServingUnit.SLICE
        "scoop", "scoops" -> ServingUnit.SCOOP
        "container", "containers" -> ServingUnit.CONTAINER
        else -> ServingUnit.SERVING
    }
}

private fun parseServingFromDescription(description: String?): Serving? {
    if (description.isNullOrBlank()) return null

    val regex =
        Regex("""Per\s+([\d.]+)\s*([a-zA-Z]+)\s+-\s+Calories:\s*([\d.]+)kcal\s*\|\s*Fat:\s*([\d.]+)g\s*\|\s*Carbs:\s*([\d.]+)g\s*\|\s*Protein:\s*([\d.]+)g""")

    val match = regex.find(description) ?: return null

    val (amount, unit, calories, fat, carbs, protein) = match.destructured

    return Serving(
        id = "default",
        description = "Per $amount $unit",
        amount = amount.toFloatOrNull() ?: 100f,
        unit = parseServingUnit(unit),
        nutrition = NutritionInfo(
            calories = calories.toFloatOrNull() ?: 0f,
            protein = protein.toFloatOrNull() ?: 0f,
            carbs = carbs.toFloatOrNull() ?: 0f,
            fat = fat.toFloatOrNull() ?: 0f
        ),
        isDefault = true
    )
}

