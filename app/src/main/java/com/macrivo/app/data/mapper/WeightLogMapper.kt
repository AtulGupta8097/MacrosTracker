package com.macrivo.app.data.mapper

import com.macrivo.app.data.local.entity.WeightLogEntity
import com.macrivo.app.data.remote.dto.firebase.WeightLogDto
import com.macrivo.app.domain.model.SyncStatus
import com.macrivo.app.domain.model.weight.WeightLog

fun WeightLogEntity.toDomain() = WeightLog(
    id = id,
    weight = weight,
    date = date,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncStatus = syncStatus,
    retryCount = retryCount,
    lastSyncAttempt = lastSyncAttempt,
)

fun WeightLog.toEntity() = WeightLogEntity(
    id = id,
    weight = weight,
    date = date,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncStatus = syncStatus,
    retryCount = retryCount,
    lastSyncAttempt = lastSyncAttempt,
)

fun WeightLogDto.toEntity(
    syncStatus: SyncStatus = SyncStatus.SYNCED,
    lastSyncAttempt: Long? = System.currentTimeMillis(),
) = WeightLogEntity(
    id = id,
    weight = weight,
    date = date,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncStatus = syncStatus,
    retryCount = 0,
    lastSyncAttempt = lastSyncAttempt,
)

fun WeightLogEntity.toFirestoreDto() =
    WeightLogDto(
        id = id,
        weight = weight,
        date = date,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
