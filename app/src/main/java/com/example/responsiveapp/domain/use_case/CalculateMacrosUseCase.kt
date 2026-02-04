package com.example.responsiveapp.domain.use_case

import com.example.responsiveapp.domain.model.MacroNutrients
import com.example.responsiveapp.domain.model.UserProfile
import com.example.responsiveapp.domain.repository.MacroRepository

class CalculateMacrosUseCase(
    private val repository: MacroRepository
) {
    operator fun invoke(profile: UserProfile): MacroNutrients {
        return repository.calculateMacros(profile)
    }
}
