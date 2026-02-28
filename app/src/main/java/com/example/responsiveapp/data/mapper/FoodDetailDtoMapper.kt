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
    metricAmount = metricServingAmount?.toFloatOrNull() ?: 100f,
    metricUnit = parseServingUnit(metricServingUnit ?: "g"),
    numberOfUnits = numberOfUnits?.toFloatOrNull() ?: 1f,
    measurementDescription = measurementDescription ?: "serving",
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
        // Not present in ServingDto — default to 0f
        polyunsaturatedFat = 0f,
        monounsaturatedFat = 0f,
        transFat = 0f,
        potassium = 0f,
        addedSugars = 0f,
        vitaminD = 0f,
        calcium = 0f,
        iron = 0f
    ),
    isDefault = isDefault
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