package com.example.responsiveapp.data.repository

import com.example.responsiveapp.data.datasource.MacroCalculatorDataSource
import com.example.responsiveapp.domain.model.MacroNutrients
import com.example.responsiveapp.domain.model.UserProfile
import com.example.responsiveapp.domain.repository.MacroRepository

class MacroRepositoryImpl(
    private val dataSource: MacroCalculatorDataSource
) : MacroRepository {

    override fun calculateMacros(profile: UserProfile): MacroNutrients {
        return dataSource.calculate(profile)
    }
}
