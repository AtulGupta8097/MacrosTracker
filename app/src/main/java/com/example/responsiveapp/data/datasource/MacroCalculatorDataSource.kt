package com.example.responsiveapp.data.datasource

import com.example.responsiveapp.domain.model.MacroNutrients
import com.example.responsiveapp.domain.model.UserProfile

interface MacroCalculatorDataSource {
    fun calculate(profile: UserProfile): MacroNutrients
}
