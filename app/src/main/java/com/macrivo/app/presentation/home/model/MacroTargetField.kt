package com.macrivo.app.presentation.home.model

enum class MacroTargetField(
    val key: String,
    val label: String,
    val unit: String,
    val min: Int,
    val max: Int,
    val step: Int,
) {
    Calories("calories", "Calories", "kcal", 800, 15000, 50),
    Protein("protein", "Protein", "g", 10, 400, 5),
    Carbs("carbs", "Carbs", "g", 20, 1000, 5),
    Fats("fats", "Fat", "g", 10, 400, 2),
    Fiber("fiber", "Fiber", "g", 5, 200, 1);

    companion object {
        fun fromKey(key: String): MacroTargetField? = entries.firstOrNull { it.key == key }
    }
}