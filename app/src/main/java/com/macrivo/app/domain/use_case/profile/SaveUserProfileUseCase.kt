package com.macrivo.app.domain.use_case.profile

import com.macrivo.app.domain.calculator.MacroCalculator
import com.macrivo.app.domain.model.NutritionTargets
import com.macrivo.app.domain.model.UserProfile
import com.macrivo.app.domain.model.macros.MacroTarget
import com.macrivo.app.domain.repository.UserProfileRepository
import com.macrivo.app.domain.use_case.macrostarget.SaveMacroTargetUseCase
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveUserProfileUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val macroCalculator: MacroCalculator,
    private val saveMacroTargetUseCase: SaveMacroTargetUseCase
) {

    suspend operator fun invoke(profile: UserProfile): NutritionTargets {

        userProfileRepository.saveUserProfile(profile)

        val result = macroCalculator.calculate(profile)

        saveMacroTargetUseCase(
            MacroTarget(
                id = UUID.randomUUID().toString(),
                targets = result.targets,
                bmr = result.bmr,
                tdee = result.tdee,
                bmi = result.bmi,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )

        return result.targets
    }
}