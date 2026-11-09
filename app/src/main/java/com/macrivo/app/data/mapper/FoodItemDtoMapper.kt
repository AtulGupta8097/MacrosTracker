package com.macrivo.app.data.mapper

import com.macrivo.app.data.remote.dto.fatsecret.search.FoodItemDto
import com.macrivo.app.domain.model.FoodItem
import com.macrivo.app.domain.model.MacroSummary

fun FoodItemDto.toDomain(): FoodItem = FoodItem(
    id = foodId,
    name = foodName,
    brand = brandName,
    foodType = foodType,
    macroSummary = parseDescriptionToMacros(foodDescription)
        ?: MacroSummary(servingLabel = "", calories = 0f, fat = 0f, carbs = 0f, protein = 0f)
)

private fun parseDescriptionToMacros(description: String?): MacroSummary? {
    if (description.isNullOrBlank()) return null

    val regex = Regex(
        """Per\s+([\d.]+\s*[a-zA-Z]+(?:\s+[a-zA-Z]+)?)\s*-\s*Calories:\s*([\d.]+)kcal\s*\|\s*Fat:\s*([\d.]+)g\s*\|\s*Carbs:\s*([\d.]+)g\s*\|\s*Protein:\s*([\d.]+)g""",
        RegexOption.IGNORE_CASE
    )

    val match = regex.find(description) ?: return null
    val (label, calories, fat, carbs, protein) = match.destructured

    return MacroSummary(
        servingLabel = label.trim(),
        calories = calories.toFloatOrNull() ?: 0f,
        fat = fat.toFloatOrNull() ?: 0f,
        carbs = carbs.toFloatOrNull() ?: 0f,
        protein = protein.toFloatOrNull() ?: 0f
    )
}