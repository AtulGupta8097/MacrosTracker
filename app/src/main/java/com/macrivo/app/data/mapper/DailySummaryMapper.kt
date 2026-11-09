package com.macrivo.app.data.mapper

import com.macrivo.app.data.local.entity.DailySummaryEntity
import com.macrivo.app.data.remote.dto.firebase.DailySummaryDto
import com.macrivo.app.domain.model.DailySummary
import com.macrivo.app.domain.model.SyncStatus

fun DailySummaryEntity.toDomain() = DailySummary(
    date = date,
    target = target,
    consumed = consumed,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun DailySummary.toEntity(
    syncStatus: SyncStatus = SyncStatus.PENDING,
    retryCount: Int = 0,
    lastSyncAttempt: Long? = null
) = DailySummaryEntity(
    date = date,
    target = target,
    consumed = consumed,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncStatus = syncStatus,
    retryCount = retryCount,
    lastSyncAttempt = lastSyncAttempt
)


fun DailySummaryDto.toEntity(
    syncStatus: SyncStatus = SyncStatus.SYNCED,
    lastSyncAttempt: Long? = System.currentTimeMillis()
) = DailySummaryEntity(
    date = date,
    target = target,
    consumed = consumed,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncStatus = syncStatus,
    retryCount = 0,
    lastSyncAttempt = lastSyncAttempt
)

fun DailySummaryEntity.toFirestoreDto() =
    DailySummaryDto(
        date = date,
        target = target,
        consumed = consumed,
        createdAt = createdAt,
        updatedAt = updatedAt
    )