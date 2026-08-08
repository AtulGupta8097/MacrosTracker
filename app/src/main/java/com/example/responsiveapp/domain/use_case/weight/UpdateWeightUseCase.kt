package com.example.responsiveapp.domain.use_case.weight

import com.example.responsiveapp.domain.model.SyncStatus
import com.example.responsiveapp.domain.model.weight.WeightLog
import com.example.responsiveapp.domain.repository.WeightRepository
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
