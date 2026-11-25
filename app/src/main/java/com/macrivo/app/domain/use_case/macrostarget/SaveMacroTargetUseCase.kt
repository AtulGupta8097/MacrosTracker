package com.macrivo.app.domain.use_case.macrostarget

import com.macrivo.app.core.utils.DateUtils
import com.macrivo.app.domain.model.macros.MacroTarget
import com.macrivo.app.domain.repository.DailySummaryRepository
import com.macrivo.app.domain.repository.MacroTargetRepository
import com.macrivo.app.sync.SyncScheduler
import com.macrivo.app.sync.SyncType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveMacroTargetUseCase @Inject constructor(
    private val macroTargetRepository: MacroTargetRepository,
    private val dailySummaryRepository: DailySummaryRepository,
    private val scheduler: SyncScheduler,
) {
    suspend operator fun invoke(target: MacroTarget) {
        macroTargetRepository.saveTarget(target)

        val today = DateUtils.today()
        dailySummaryRepository.getForDate(today)?.let { summary ->
            dailySummaryRepository.update(
                summary.copy(target = target.targets, updatedAt = System.currentTimeMillis())
            )
            scheduler.schedule(SyncType.DAILY_SUMMARY)
        }
    }
}
