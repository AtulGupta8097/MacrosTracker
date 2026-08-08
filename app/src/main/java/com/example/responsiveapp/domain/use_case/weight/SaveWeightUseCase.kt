package com.example.responsiveapp.domain.use_case.weight

import com.example.responsiveapp.domain.model.SyncStatus
import com.example.responsiveapp.domain.model.weight.WeightLog
import com.example.responsiveapp.domain.repository.WeightRepository
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
