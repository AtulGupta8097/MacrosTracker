package com.example.responsiveapp.data.datasource.local

import com.example.responsiveapp.data.datasource.MacroCalculatorDataSource
import com.example.responsiveapp.domain.model.ActivityLevel
import com.example.responsiveapp.domain.model.Gender
import com.example.responsiveapp.domain.model.Goal
import com.example.responsiveapp.domain.model.MacroNutrients
import com.example.responsiveapp.domain.model.UserProfile
import kotlin.math.roundToInt

class MacroCalculatorDataSourceImpl : MacroCalculatorDataSource {

    override fun calculate(profile: UserProfile): MacroNutrients {

        val bmr = when (profile.gender) {
            Gender.MALE ->
                (10 * profile.weight + 6.25 * profile.height - 5 * profile.age + 5).roundToInt()
            Gender.FEMALE ->
                (10 * profile.weight + 6.25 * profile.height - 5 * profile.age - 161).roundToInt()
        }

        val activityMultiplier = when (profile.activityLevel) {
            ActivityLevel.SEDENTARY -> 1.2
            ActivityLevel.LIGHT -> 1.375
            ActivityLevel.MODERATE -> 1.55
            ActivityLevel.ACTIVE -> 1.725
            ActivityLevel.VERY_ACTIVE -> 1.9
        }

        val tdee = (bmr * activityMultiplier).roundToInt()

        val calories = when (profile.goal) {
            Goal.LOSE -> (tdee * 0.8).roundToInt()
            Goal.MAINTAIN -> tdee
            Goal.GAIN -> (tdee * 1.15).roundToInt()
        }

        val protein = when (profile.goal) {
            Goal.LOSE -> (profile.weight * 2.2).roundToInt()
            else -> (profile.weight * 2.0).roundToInt()
        }

        val fats = (calories * 0.25 / 9).roundToInt()
        val carbs = ((calories - protein * 4 - fats * 9) / 4)

        return MacroNutrients(
            calories = calories,
            protein = protein,
            carbs = carbs,
            fats = fats,
            bmr = bmr
        )
    }
}
