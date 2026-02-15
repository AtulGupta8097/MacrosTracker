package com.example.responsiveapp.domain.use_case

import com.example.responsiveapp.domain.model.MacroNutrients
import com.example.responsiveapp.domain.model.UserProfile
import com.example.responsiveapp.domain.repository.MacroRepository
import javax.inject.Inject

class CalculateMacrosUseCase @Inject constructor(
    private val repository: MacroRepository
) {
    operator fun invoke(profile: UserProfile): MacroNutrients {
        return repository.calculateMacros(profile)
    }
}
