package com.example.responsiveapp.data.mapper

import com.example.responsiveapp.data.local.entity.FoodDetailEntity
import com.example.responsiveapp.data.local.entity.FoodItemEntity
import com.example.responsiveapp.data.local.entity.ServingEntity
import com.example.responsiveapp.domain.model.FoodDetail
import com.example.responsiveapp.domain.model.FoodItem
import com.example.responsiveapp.domain.model.MacroSummary
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.domain.model.ServingUnit

fun FoodItemEntity.toDomain(): FoodItem = FoodItem(
    id = id,
    name = name,
    brand = brand,
    foodType = foodType,
    macroSummary = MacroSummary(
        servingLabel = servingLabel ?: "",
        calories = calories ?: 0f,
        fat = fat ?: 0f,
        carbs = carbs ?: 0f,
        protein = protein ?: 0f
    )
)

fun FoodItem.toEntity(): FoodItemEntity = FoodItemEntity(
    id = id,
    name = name,
    brand = brand,
    foodType = foodType,
    servingLabel = macroSummary.servingLabel,
    calories = macroSummary.calories,
    fat = macroSummary.fat,
    carbs = macroSummary.carbs,
    protein = macroSummary.protein
)

fun FoodDetailEntity.toDomain(): FoodDetail = FoodDetail(
    id = id,
    name = name,
    brand = brand,
    foodType = foodType,
    imageUrl = imageUrl,
    barcode = barcode,
    isVerified = isVerified,
    servings = servings.map { it.toDomain() }
)

fun FoodDetail.toEntity(): FoodDetailEntity = FoodDetailEntity(
    id = id,
    name = name,
    brand = brand,
    foodType = foodType,
    imageUrl = imageUrl,
    barcode = barcode,
    isVerified = isVerified,
    servings = servings.map { it.toEntity() }
)
fun ServingEntity.toDomain(): Serving = Serving(
    id = id,
    description = description,
    metricAmount = metricAmount,
    metricUnit = ServingUnit.valueOf(metricUnit),
    numberOfUnits = numberOfUnits,
    measurementDescription = measurementDescription,
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
        polyunsaturatedFat = polyunsaturatedFat,
        monounsaturatedFat = monounsaturatedFat,
        transFat = transFat,
        potassium = potassium,
        addedSugars = addedSugars,
        vitaminD = vitaminD,
        calcium = calcium,
        iron = iron
    ),
    isDefault = isDefault
)

fun Serving.toEntity(): ServingEntity = ServingEntity(
    id = id,
    description = description,
    metricAmount = metricAmount,
    metricUnit = metricUnit.name,
    numberOfUnits = numberOfUnits,
    measurementDescription = measurementDescription,
    calories = nutrition.calories,
    protein = nutrition.protein,
    carbs = nutrition.carbs,
    fat = nutrition.fat,
    fiber = nutrition.fiber,
    sugar = nutrition.sugar,
    sodium = nutrition.sodium,
    cholesterol = nutrition.cholesterol,
    saturatedFat = nutrition.saturatedFat,
    polyunsaturatedFat = nutrition.polyunsaturatedFat,
    monounsaturatedFat = nutrition.monounsaturatedFat,
    transFat = nutrition.transFat,
    potassium = nutrition.potassium,
    addedSugars = nutrition.addedSugars,
    vitaminD = nutrition.vitaminD,
    calcium = nutrition.calcium,
    iron = nutrition.iron,
    isDefault = isDefault
)