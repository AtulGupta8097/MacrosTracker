package com.macrivo.app.domain.use_case.dailysummary

import com.macrivo.app.domain.model.DailySummary
import com.macrivo.app.domain.repository.DailySummaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveDailySummaryForDateUseCase @Inject constructor(
    private val dailySummaryRepository: DailySummaryRepository
) {
    operator fun invoke(date: Long): Flow<DailySummary?> =
        dailySummaryRepository.observeForDate(date)
}
