package com.macrivo.app.domain.use_case.weight

import com.macrivo.app.domain.model.SyncStatus
import com.macrivo.app.domain.model.weight.WeightLog
import com.macrivo.app.domain.repository.WeightRepository
import javax.inject.Inject

class UpdateWeightUseCase @Inject constructor(
    private val weightRepository: WeightRepository,
) {
    suspend operator fun invoke(weightLog: WeightLog) {

        val updated = weightLog.copy(
            updatedAt = System.currentTimeMillis(),
            syncStatus = SyncStatus.PENDING,
        )

        weightRepository.updateWeight(updated)
    }
}
