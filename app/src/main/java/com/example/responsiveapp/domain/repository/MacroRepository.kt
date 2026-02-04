package com.example.responsiveapp.domain.repository

import com.example.responsiveapp.domain.model.MacroNutrients
import com.example.responsiveapp.domain.model.UserProfile

interface MacroRepository {
    fun calculateMacros(profile: UserProfile): MacroNutrients
}
