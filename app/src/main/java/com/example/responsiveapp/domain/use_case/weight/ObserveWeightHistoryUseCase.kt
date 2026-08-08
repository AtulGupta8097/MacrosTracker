package com.example.responsiveapp.domain.use_case.weight

import com.example.responsiveapp.domain.model.weight.WeightLog
import com.example.responsiveapp.domain.repository.WeightRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWeightHistoryUseCase @Inject constructor(
    private val weightRepository: WeightRepository,
) {
    operator fun invoke(): Flow<List<WeightLog>> =
        weightRepository.observeWeightHistory()
}
