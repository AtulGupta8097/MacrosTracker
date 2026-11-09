package com.macrivo.app.domain.use_case.weight

import com.macrivo.app.domain.model.SyncStatus
import com.macrivo.app.domain.model.weight.WeightLog
import com.macrivo.app.domain.repository.WeightRepository
import java.util.UUID
import javax.inject.Inject

class SaveWeightUseCase @Inject constructor(
    private val weightRepository: WeightRepository,
) {
    suspend operator fun invoke(weight: Float, date: Long): WeightLog {

        val now = System.currentTimeMillis()

        val weightLog = WeightLog(
            id = UUID.randomUUID().toString(),
            weight = weight,
            date = date,
            createdAt = now,
            updatedAt = now,
            syncStatus = SyncStatus.PENDING,
        )

        weightRepository.saveWeight(weightLog)

        return weightLog
    }
}
