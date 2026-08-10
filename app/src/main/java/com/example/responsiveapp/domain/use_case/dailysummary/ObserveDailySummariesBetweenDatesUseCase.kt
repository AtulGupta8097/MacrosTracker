package com.example.responsiveapp.domain.use_case.dailysummary

import com.example.responsiveapp.domain.model.DailySummary
import com.example.responsiveapp.domain.repository.DailySummaryRepository
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