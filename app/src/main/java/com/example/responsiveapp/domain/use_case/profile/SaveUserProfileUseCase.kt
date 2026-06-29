package com.example.responsiveapp.domain.use_case.profile

import com.example.responsiveapp.domain.calculator.MacroCalculator
import com.example.responsiveapp.domain.model.NutritionTargets
import com.example.responsiveapp.domain.model.UserProfile
import com.example.responsiveapp.domain.model.macros.MacroTarget
import com.example.responsiveapp.domain.repository.UserProfileRepository
import com.example.responsiveapp.domain.use_case.macrostarget.SaveMacroTargetUseCase
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveUserProfileUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val macroCalculator: MacroCalculator,
    private val saveMacroTargetUseCase: SaveMacroTargetUseCase
) {
    suspend operator fun invoke(profile: UserProfile): Result<NutritionTargets> {
        return try {

            userProfileRepository
                .saveUserProfile(profile)
                .getOrThrow()

            val result = macroCalculator.calculate(profile)

            saveMacroTargetUseCase(
                MacroTarget(
                    id        = UUID.randomUUID().toString(),
                    targets   = result.targets,
                    bmr       = result.bmr,
                    tdee      = result.tdee,
                    createdAt = System.currentTimeMillis()
                )
            ).getOrThrow()

            Result.success(result.targets)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}