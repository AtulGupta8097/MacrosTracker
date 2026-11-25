
package com.macrivo.app.domain.calculator

import com.macrivo.app.core.utils.HealthMetricsUtils
import com.macrivo.app.domain.model.ActivityLevel
import com.macrivo.app.domain.model.Gender
import com.macrivo.app.domain.model.Goal
import com.macrivo.app.domain.model.NutritionTargets
import com.macrivo.app.domain.model.UserProfile
import com.macrivo.app.domain.model.macros.CalculationResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Singleton
class MacroCalculator @Inject constructor() {

    fun calculate(profile: UserProfile): CalculationResult {

        val weight = profile.weight.toDouble()
        val height = profile.height.toDouble()
        val bmi = HealthMetricsUtils.calculateBmi(
            profile.height,
            profile.weight
        ) ?: 0f

        // 1. BMR - Mifflin-St Jeor
        val bmrRaw = 10.0 * weight +
                6.25 * height -
                5.0 * profile.age +
                if (profile.gender == Gender.MALE) 5.0 else -161.0

        val bmr = bmrRaw.roundToInt()

        // 2. TDEE
        val tdee = (
                bmrRaw * activityFactor(profile.activityLevel)
                ).roundToInt()

        // 3. Calories
        val calories = goalCalories(
            profile,
            bmrRaw,
            tdee.toDouble(),
            bmi.toDouble()
        )

        // Reference weight: above a healthy BMI,
        // protein and fat are based on adjusted body weight.
        val heightM = height / 100.0
        val healthyMax = 25.0 * heightM * heightM

        val refWeight = if (weight > healthyMax) {
            healthyMax + 0.25 * (weight - healthyMax)
        } else {
            weight
        }

        // 4. Protein
        val perKg = when (profile.goal) {
            Goal.LOSE -> 2.0
            Goal.GAIN -> 1.8
            Goal.MAINTAIN -> 1.6
        } + when (profile.activityLevel) {
            ActivityLevel.SEDENTARY -> -0.4
            ActivityLevel.LIGHT -> -0.2
            else -> 0.0
        }

        var protein = (refWeight * perKg)
            .coerceAtMost(calories * 0.35 / 4.0)
            .coerceAtLeast(refWeight * 0.8)

        // 5. Fat
        var fat = max(
            calories * 0.25 / 9.0,
            refWeight * 0.6
        ).coerceAtMost(calories * 0.35 / 9.0)

        // 6. Carbs: the rest, but not below MIN_CARBS_G
        var carbs = (
                calories - protein * 4.0 - fat * 9.0
                ) / 4.0

        if (carbs < MIN_CARBS_G) {
            val fatFloor = max(
                calories * 0.20 / 9.0,
                refWeight * 0.5
            )

            fat = max(
                fatFloor,
                (calories - protein * 4.0 - MIN_CARBS_G * 4.0) / 9.0
            )

            carbs = (
                    calories - protein * 4.0 - fat * 9.0
                    ) / 4.0

            if (carbs < MIN_CARBS_G) {
                protein = max(
                    refWeight * 1.2,
                    (calories - fat * 9.0 - MIN_CARBS_G * 4.0) / 4.0
                )

                carbs = (
                        calories - protein * 4.0 - fat * 9.0
                        ) / 4.0
            }
        }

        val fiber = max(
            25.0,
            calories / 1000.0 * 14.0
        ).roundToInt()

        val sugarLimit = (calories * 0.10 / 4.0).roundToInt()

        val targets = NutritionTargets(
            calories = calories,
            protein = protein.roundToInt(),
            carbs = carbs.coerceAtLeast(0.0).roundToInt(),
            fats = fat.roundToInt(),
            fiber = fiber,
            sugarLimit = sugarLimit,
            sodiumLimit = SODIUM_LIMIT_MG
        )

        return CalculationResult(
            targets = targets,
            bmr = bmr,
            tdee = tdee,
            bmi = bmi
        )
    }

    private fun activityFactor(level: ActivityLevel) = when (level) {
        ActivityLevel.SEDENTARY -> 1.2
        ActivityLevel.LIGHT -> 1.375
        ActivityLevel.MODERATE -> 1.55
        ActivityLevel.ACTIVE -> 1.725
        ActivityLevel.VERY_ACTIVE -> 1.9
    }

    /**
     * Calories for the goal, from a safe weekly rate of body-weight change.
     */
    private fun goalCalories(
        profile: UserProfile,
        bmr: Double,
        tdee: Double,
        bmi: Double
    ): Int {

        val weight = profile.weight.toDouble()
        val diff = (profile.targetWeight - profile.weight).toDouble()

        return when (profile.goal) {

            Goal.MAINTAIN -> tdee.roundToInt()

            Goal.LOSE -> {
                val toLose = max(0.0, -diff)

                // Percentage of body weight per week:
                // faster when there is a lot to lose, gentler near the target.
                val weeklyRate = when {
                    toLose <= 0.5 -> 0.0025
                    toLose < 5.0 -> 0.005
                    bmi >= 30.0 -> 0.010
                    else -> 0.0075
                }

                val deficit = (
                        weight * weeklyRate * KCAL_PER_KG / 7.0
                        )
                    .coerceAtMost(tdee * 0.25)
                    .coerceAtMost(1000.0)

                val floor = max(
                    bmr,
                    if (profile.gender == Gender.MALE) 1500.0 else 1200.0
                )

                max(
                    tdee - deficit,
                    min(floor, tdee)
                ).roundToInt()
            }

            Goal.GAIN -> {
                val toGain = max(0.0, diff)

                val weeklyRate = when {
                    toGain <= 0.5 -> 0.0
                    bmi < 18.5 -> 0.005
                    bmi < 25.0 -> 0.0035
                    else -> 0.002
                }

                val surplus = if (weeklyRate == 0.0) {
                    150.0
                } else {
                    weight * weeklyRate * KCAL_PER_KG / 7.0
                }

                (
                        tdee + surplus
                            .coerceIn(150.0, 500.0)
                            .coerceAtMost(tdee * 0.15)
                        ).roundToInt()
            }
        }
    }

    private companion object {
        const val KCAL_PER_KG = 7700.0
        const val MIN_CARBS_G = 100.0
        const val SODIUM_LIMIT_MG = 2300
    }
}
