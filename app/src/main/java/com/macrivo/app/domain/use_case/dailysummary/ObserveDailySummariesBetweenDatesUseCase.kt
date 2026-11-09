package com.macrivo.app.domain.use_case.dailysummary

import com.macrivo.app.domain.model.DailySummary
import com.macrivo.app.domain.repository.DailySummaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveDailySummariesBetweenDatesUseCase @Inject constructor(
    private val dailySummaryRepository: DailySummaryRepository
) {

    operator fun invoke(
        startDate: Long,
        endDate: Long
    ): Flow<List<DailySummary>> {
        return dailySummaryRepository.observeSummariesBetweenDates(
            startDate = startDate,
            endDate = endDate
        )
    }
}