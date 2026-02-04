package com.example.responsiveapp.core.di

import com.example.responsiveapp.data.datasource.MacroCalculatorDataSource
import com.example.responsiveapp.data.datasource.local.MacroCalculatorDataSourceImpl
import com.example.responsiveapp.data.repository.MacroRepositoryImpl
import com.example.responsiveapp.domain.repository.MacroRepository
import com.example.responsiveapp.domain.use_case.CalculateMacrosUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MacroModule {

    @Provides
    fun provideMacroCalculatorDataSource(): MacroCalculatorDataSource =
        MacroCalculatorDataSourceImpl()

    @Provides
    fun provideMacroRepository(
        dataSource: MacroCalculatorDataSource
    ): MacroRepository =
        MacroRepositoryImpl(dataSource)

    @Provides
    fun provideCalculateMacrosUseCase(
        repository: MacroRepository
    ): CalculateMacrosUseCase =
        CalculateMacrosUseCase(repository)
}
