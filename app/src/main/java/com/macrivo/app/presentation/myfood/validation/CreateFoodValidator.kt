package com.macrivo.app.presentation.myfood.validation

import androidx.compose.runtime.Immutable
import com.macrivo.app.presentation.myfood.BasicInfoState
import com.macrivo.app.presentation.myfood.MacronutrientsState
import com.macrivo.app.presentation.myfood.MineralsState
import com.macrivo.app.presentation.myfood.VitaminsState
import java.math.BigDecimal
import kotlin.math.abs
import kotlin.math.max

enum class NutrientField(val label: String, val unit: String, val required: Boolean = false) {
    CALORIES("Calories", "kcal", required = true),
    PROTEIN("Protein", "g"),
    CARBOHYDRATES("Carbohydrates", "g"),
    TOTAL_FAT("Total Fat", "g"),
    FIBER("Fiber", "g"),
    SUGAR("Sugar", "g"),
    SODIUM("Sodium", "mg"),
    CHOLESTEROL("Cholesterol", "mg"),
    POTASSIUM("Potassium", "mg"),
    CALCIUM("Calcium", "mg"),
    IRON("Iron", "mg"),
    VITAMIN_A("Vitamin A", "mcg"),
    VITAMIN_C("Vitamin C", "mg"),
    VITAMIN_D("Vitamin D", "mcg"),
}

@Immutable
data class BasicInfoErrors(
    val foodName: String? = null,
    val servingAmount: String? = null,
    val servingUnit: String? = null,
    val servingsPerContainer: String? = null,
) {
    val hasErrors: Boolean
        get() = foodName != null || servingAmount != null ||
            servingUnit != null || servingsPerContainer != null
}

@Immutable
data class NutrientErrors(val byField: Map<NutrientField, String> = emptyMap()) {
    val hasErrors: Boolean get() = byField.isNotEmpty()
    operator fun get(field: NutrientField): String? = byField[field]
}

/** Result of parsing one numeric text input. Never silently falls back to 0. */
sealed interface ParsedNumber {
    data class Value(val value: Float) : ParsedNumber
    data object Empty : ParsedNumber
    data class Invalid(val message: String) : ParsedNumber
}

object CreateFoodValidator {

    const val MAX_NUTRIENT_VALUE = 100_000f
    const val MAX_SERVING_AMOUNT = 100_000f
    const val MAX_SERVINGS_PER_CONTAINER = 10_000f
    const val MAX_NAME_LENGTH = 80

    // Digits with an optional single decimal point. Rejects "-5", "1e3", "abc", "1.2.3", ".", "+1".
    private val NUMBER = Regex("^(\\d+(\\.\\d*)?|\\.\\d+)$")

    fun parseNumber(raw: String): ParsedNumber {
        val text = raw.trim()
        if (text.isEmpty()) return ParsedNumber.Empty
        if (text.startsWith("-")) return ParsedNumber.Invalid("Can't be negative")
        if (!NUMBER.matches(text)) return ParsedNumber.Invalid("Enter a valid number")
        val value = text.toFloatOrNull()
        if (value == null || value.isNaN() || value.isInfinite()) {
            return ParsedNumber.Invalid("Enter a valid number")
        }
        return ParsedNumber.Value(value)
    }

    // ---------------------------------------------------------------- Basic info

    fun validateBasicInfo(info: BasicInfoState): BasicInfoErrors {
        val name = info.foodName.trim()
        val nameError = when {
            name.isEmpty() -> "Food name is required"
            name.length > MAX_NAME_LENGTH -> "Keep the name under $MAX_NAME_LENGTH characters"
            else -> null
        }

        val amountError = when (val p = parseNumber(info.servingSize)) {
            ParsedNumber.Empty -> "Serving size is required"
            is ParsedNumber.Invalid -> p.message
            is ParsedNumber.Value -> when {
                p.value <= 0f -> "Must be greater than 0"
                p.value > MAX_SERVING_AMOUNT -> "That's too large"
                else -> null
            }
        }

        val unit = info.servingUnit.trim()
        val unitError = when {
            unit.isEmpty() -> "Add a unit (g, ml, cup, piece…)"
            unit.any { it.isDigit() } -> "Unit can't contain numbers"
            unit.length > 20 -> "Unit is too long"
            else -> null
        }

        // Optional: blank is allowed (the model stores 0 = "not specified"), but if the
        // user typed something it must be a valid positive number.
        val servingsError = when (val p = parseNumber(info.servingsPerContainer)) {
            ParsedNumber.Empty -> null
            is ParsedNumber.Invalid -> p.message
            is ParsedNumber.Value -> when {
                p.value <= 0f -> "Must be greater than 0"
                p.value > MAX_SERVINGS_PER_CONTAINER -> "That's too large"
                else -> null
            }
        }

        return BasicInfoErrors(nameError, amountError, unitError, servingsError)
    }

    // ----------------------------------------------------------------- Nutrients

    fun nutrientInputs(
        macros: MacronutrientsState,
        minerals: MineralsState,
        vitamins: VitaminsState,
    ): Map<NutrientField, String> = linkedMapOf(
        NutrientField.CALORIES to macros.calories,
        NutrientField.PROTEIN to macros.protein,
        NutrientField.CARBOHYDRATES to macros.carbohydrates,
        NutrientField.TOTAL_FAT to macros.totalFat,
        NutrientField.FIBER to macros.fiber,
        NutrientField.SUGAR to macros.sugar,
        NutrientField.SODIUM to minerals.sodium,
        NutrientField.CHOLESTEROL to minerals.cholesterol,
        NutrientField.POTASSIUM to minerals.potassium,
        NutrientField.CALCIUM to minerals.calcium,
        NutrientField.IRON to minerals.iron,
        NutrientField.VITAMIN_A to vitamins.vitaminA,
        NutrientField.VITAMIN_C to vitamins.vitaminC,
        NutrientField.VITAMIN_D to vitamins.vitaminD,
    )

    /**
     * Policy: calories are required; every other nutrient is optional and a blank field
     * means "not provided" (stored as 0). Anything that is typed must be a valid number >= 0.
     */
    fun validateNutrients(
        macros: MacronutrientsState,
        minerals: MineralsState,
        vitamins: VitaminsState,
    ): NutrientErrors {
        val errors = linkedMapOf<NutrientField, String>()
        nutrientInputs(macros, minerals, vitamins).forEach { (field, raw) ->
            when (val p = parseNumber(raw)) {
                ParsedNumber.Empty -> if (field.required) errors[field] = "${field.label} is required"
                is ParsedNumber.Invalid -> errors[field] = p.message
                is ParsedNumber.Value ->
                    if (p.value > MAX_NUTRIENT_VALUE) errors[field] = "That's too large"
            }
        }
        return NutrientErrors(errors)
    }

    /** Parses a field that has already passed validation. Blank -> 0 (documented policy). */
    fun valueOrZero(raw: String): Float =
        (parseNumber(raw) as? ParsedNumber.Value)?.value ?: 0f

    // ------------------------------------------------------------ Calorie warning

    /**
     * Soft consistency hint: 4*protein + 4*carbs + 9*fat vs. entered calories.
     * Never blocks saving and never changes what the user typed. The tolerance
     * (max of 20 kcal or 20%) absorbs rounding, fiber and alcohol.
     */
    fun calorieWarning(macros: MacronutrientsState): String? {
        val calories = (parseNumber(macros.calories) as? ParsedNumber.Value)?.value ?: return null
        val inputs = listOf(macros.protein, macros.carbohydrates, macros.totalFat)
        if (inputs.all { parseNumber(it) == ParsedNumber.Empty }) return null
        // Blank macro = 0; an invalid macro => no warning (the field error is shown instead).
        val parsed = inputs.map {
            when (val p = parseNumber(it)) {
                ParsedNumber.Empty -> 0f
                is ParsedNumber.Value -> p.value
                is ParsedNumber.Invalid -> return null
            }
        }
        val estimate = 4f * parsed[0] + 4f * parsed[1] + 9f * parsed[2]
        val tolerance = max(20f, 0.2f * max(calories, estimate))
        if (abs(calories - estimate) <= tolerance) return null
        return "Calories (${formatNumber(calories)} kcal) don't quite match the macros " +
            "(≈ ${formatNumber(estimate)} kcal). You can still save."
    }

    // ------------------------------------------------------------- Serving string

    /** "100 g" style string stored in CustomFood.servingSize. */
    fun combineServing(amount: String, unit: String): String =
        "${amount.trim()} ${unit.trim()}".trim()

    private val SERVING = Regex("^\\s*(\\d+(?:\\.\\d+)?|\\.\\d+)\\s*(.*)$")

    /** Splits a stored serving string ("100 g", "1 cup", "250ml") into (amount, unit). */
    fun splitServing(stored: String): Pair<String, String> {
        val m = SERVING.matchEntire(stored) ?: return "" to stored.trim()
        return m.groupValues[1] to m.groupValues[2].trim()
    }

    /** Float -> plain text without exponent / trailing zeros: 3.6 -> "3.6", 1.0E7 -> "10000000". */
    fun formatNumber(value: Float): String =
        BigDecimal(value.toString()).stripTrailingZeros().toPlainString()
}
