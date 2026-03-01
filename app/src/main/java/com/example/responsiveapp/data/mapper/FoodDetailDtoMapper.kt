package com.example.responsiveapp.data.mapper

import com.example.responsiveapp.data.remote.dto.fatsecret.detail.FoodDetailDto
import com.example.responsiveapp.data.remote.dto.fatsecret.detail.ServingDto
import com.example.responsiveapp.domain.model.FoodDetail
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.domain.model.ServingUnit

fun FoodDetailDto.toDomain(): FoodDetail = FoodDetail(
    id = foodId,
    name = foodName,
    brand = brandName,
    foodType = foodType,
    servings = servings.serving.mapIndexed { index, servingDto ->
        servingDto.toDomain(isDefault = index == 0)
    }
)

fun ServingDto.toDomain(isDefault: Boolean = false): Serving = Serving(
    id = servingId,
    description = servingDescription,
    metricAmount = metricServingAmount ?: 100f,
    metricUnit = parseServingUnit(metricServingUnit ?: "g"),
    numberOfUnits = numberOfUnits ?: 1f,
    measurementDescription = measurementDescription ?: "serving",
    nutrition = NutritionInfo(
        calories = calories ?: 0f,
        protein = protein ?: 0f,
        carbs = carbohydrate ?: 0f,
        fat = fat ?: 0f,
        fiber = fiber ?: 0f,
        sugar = sugar ?: 0f,
        sodium = sodium ?: 0f,
        cholesterol = cholesterol ?: 0f,
        saturatedFat = saturatedFat ?: 0f,
        polyunsaturatedFat = polyunsaturatedFat ?: 0f,
        monounsaturatedFat = monounsaturatedFat ?: 0f,
        transFat = transFat ?: 0f,
        potassium = potassium ?: 0f,
        addedSugars = addedSugars ?: 0f,
        vitaminA = vitaminA ?: 0f,
        vitaminC = vitaminC ?: 0f,
        vitaminD = vitaminD ?: 0f,
        calcium = calcium ?: 0f,
        iron = iron ?: 0f,
    ),
    isDefault = isDefault,
)

internal fun parseServingUnit(unitString: String): ServingUnit {
    return when (unitString.trim().lowercase()) {
        "g", "gram", "grams" -> ServingUnit.GRAM
        "kg", "kilogram", "kilograms" -> ServingUnit.KILOGRAM
        "oz", "ounce", "ounces" -> ServingUnit.OUNCE
        "lb", "lbs", "pound", "pounds" -> ServingUnit.POUND
        "ml", "milliliter", "milliliters", "millilitre", "millilitres" -> ServingUnit.MILLILITER
        "l", "liter", "liters", "litre", "litres" -> ServingUnit.LITER
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