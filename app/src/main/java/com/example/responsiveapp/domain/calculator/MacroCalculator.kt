package com.example.responsiveapp.domain.calculator

import com.example.responsiveapp.domain.model.ActivityLevel
import com.example.responsiveapp.domain.model.Gender
import com.example.responsiveapp.domain.model.Goal
import com.example.responsiveapp.domain.model.NutritionTargets
import com.example.responsiveapp.domain.model.UserProfile
import com.example.responsiveapp.domain.model.macros.CalculationResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class MacroCalculator @Inject constructor() {

    fun calculate(profile: UserProfile): CalculationResult {

        val currentWeight = profile.weight
        val targetWeight  = profile.targetWeight

        val bmr = when (profile.gender) {
            Gender.MALE ->
                (10 * currentWeight + 6.25 * profile.height - 5 * profile.age + 5).roundToInt()
            Gender.FEMALE ->
                (10 * currentWeight + 6.25 * profile.height - 5 * profile.age - 161).roundToInt()
        }

        val activityMultiplier = when (profile.activityLevel) {
            ActivityLevel.SEDENTARY  -> 1.2
            ActivityLevel.LIGHT      -> 1.375
            ActivityLevel.MODERATE   -> 1.55
            ActivityLevel.ACTIVE     -> 1.725
            ActivityLevel.VERY_ACTIVE -> 1.9
        }

        val tdee = (bmr * activityMultiplier).roundToInt()

        val weightDifference = targetWeight - currentWeight

        val calorieMultiplier = when (profile.goal) {
            Goal.LOSE -> when {
                weightDifference <= -20 -> 0.75
                weightDifference <= -10 -> 0.80
                weightDifference <= -5  -> 0.85
                else                    -> 0.90
            }
            Goal.MAINTAIN -> 1.0
            Goal.GAIN -> when {
                weightDifference >= 20 -> 1.20
                weightDifference >= 10 -> 1.15
                weightDifference >= 5  -> 1.10
                else                   -> 1.05
            }
        }

        val calories = (tdee * calorieMultiplier).roundToInt()

        val protein = when (profile.goal) {
            Goal.LOSE     -> (currentWeight * 2.0f).roundToInt()
            Goal.MAINTAIN -> (currentWeight * 1.8f).roundToInt()
            Goal.GAIN     -> (currentWeight * 1.8f).roundToInt()
        }

        val fats  = (currentWeight * 0.8f).roundToInt()
        val carbs = (calories - protein * 4 - fats * 9) / 4

        val fiber      = ((calories / 1000f) * 14).roundToInt()
        val sugarLimit = ((calories * 0.10f) / 4f).roundToInt()
        val sodiumLimit = 2300

        val targets = NutritionTargets(
            calories = calories,
            protein = protein,
            carbs = carbs,
            fats = fats,
            fiber = fiber,
            sugarLimit = sugarLimit,
            sodiumLimit = sodiumLimit
        )

        return CalculationResult(targets = targets, bmr = bmr, tdee = tdee)
    }
}