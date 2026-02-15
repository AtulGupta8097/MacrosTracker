package com.example.responsiveapp.data.mapper

import com.example.responsiveapp.data.local.entity.FoodEntity
import com.example.responsiveapp.data.local.entity.FoodLogEntity
import com.example.responsiveapp.data.local.entity.ServingEntity
import com.example.responsiveapp.data.remote.dto.fatsecret.detail.FoodDetailDto
import com.example.responsiveapp.data.remote.dto.fatsecret.detail.ServingDto
import com.example.responsiveapp.data.remote.dto.fatsecret.search.FoodItemDto
import com.example.responsiveapp.data.remote.dto.firebase.FirebaseFoodLogDto
import com.example.responsiveapp.domain.model.Food
import com.example.responsiveapp.domain.model.FoodLog
import com.example.responsiveapp.domain.model.LogMethod
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.domain.model.ServingUnit

fun FoodItemDto.toDomain(): Food {
    return Food(
        id = foodId,
        name = foodName,
        brand = brandName,
        servings = emptyList(),
        imageUrl = null,
        barcode = null,
        isVerified = true,  // FatSecret data is verified
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
 * ========== FIREBASE DTO → DOMAIN ==========
 */

fun FirebaseFoodLogDto.toDomain(): FoodLog {
    return FoodLog(
        id = id,
        userId = userId,
        foodId = foodId,
        foodName = foodName,
        brand = brand,
        servingId = servingId,
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
        date = date,
        logMethod = LogMethod.valueOf(logMethod),
        notes = notes,
        imageUrl = imageUrl,
        createdAt = System.currentTimeMillis(),
        isSynced = syncStatus == "SYNCED"
    )
}

/**
 * ========== DOMAIN → FIREBASE DTO ==========
 */

fun FoodLog.toFirebaseDto(): FirebaseFoodLogDto {
    return FirebaseFoodLogDto(
        id = id,
        userId = userId,
        foodId = foodId,
        foodName = foodName,
        brand = brand,
        servingId = servingId,
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
        date = date,
        logMethod = logMethod.name,
        notes = notes,
        imageUrl = imageUrl,
        // createdAt and updatedAt will be set by @ServerTimestamp in Firebase
        createdAt = null,
        updatedAt = null,
        syncStatus = if (isSynced) "SYNCED" else "PENDING"
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

fun FoodLogEntity.toDomain(): FoodLog {
    return FoodLog(
        id = id,
        userId = userId,
        foodId = foodId,
        foodName = foodName,
        brand = brand,
        servingId = servingId,
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
        date = date,
        logMethod = LogMethod.valueOf(logMethod),
        notes = notes,
        imageUrl = imageUrl,
        createdAt = createdAt,
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

fun FoodLog.toEntity(): FoodLogEntity {
    return FoodLogEntity(
        id = id,
        userId = userId,
        foodId = foodId,
        foodName = foodName,
        brand = brand,
        servingId = servingId,
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
        date = date,
        logMethod = logMethod.name,
        notes = notes,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = createdAt,
        syncStatus = if (isSynced) "SYNCED" else "PENDING"
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