package com.example.responsiveapp.domain.use_case.macrostarget

import com.example.responsiveapp.domain.model.macros.MacroTarget
import com.example.responsiveapp.domain.repository.MacroTargetRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveMacroTargetUseCase @Inject constructor(
    private val macroTargetRepository: MacroTargetRepository
) {
    suspend operator fun invoke(target: MacroTarget) {
        macroTargetRepository.saveTarget(target)
    }
}