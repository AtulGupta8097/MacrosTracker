package com.macrivo.app.domain.use_case.macrostarget

import com.macrivo.app.domain.model.macros.MacroTarget
import com.macrivo.app.domain.repository.MacroTargetRepository
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