package com.macrivo.app.domain.use_case.weight

import com.macrivo.app.domain.model.weight.WeightLog
import com.macrivo.app.domain.repository.WeightRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWeightHistoryUseCase @Inject constructor(
    private val weightRepository: WeightRepository,
) {
    operator fun invoke(): Flow<List<WeightLog>> =
        weightRepository.observeWeightHistory()
}
