package com.macrivo.app.domain.use_case.macrostarget

import com.macrivo.app.domain.model.macros.MacroTarget
import com.macrivo.app.domain.repository.MacroTargetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveCurrentMacroTargetUseCase @Inject constructor(
    private val macroTargetRepository: MacroTargetRepository
) {
    operator fun invoke(): Flow<MacroTarget?> = macroTargetRepository.observeCurrentTarget()
}
