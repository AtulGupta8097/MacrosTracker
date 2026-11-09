package com.macrivo.app.data.mapper

import com.macrivo.app.data.local.entity.MacroTargetEntity
import com.macrivo.app.data.remote.dto.firebase.MacroTargetDto
import com.macrivo.app.domain.model.SyncStatus
import com.macrivo.app.domain.model.macros.MacroTarget

fun MacroTarget.toEntity() =
    MacroTargetEntity(
        id = id,
        targets = targets,
        bmr = bmr,
        tdee = tdee,
        bmi = bmi,
        createdAt = createdAt,
        updatedAt = updatedAt,
        syncStatus = syncStatus,
        retryCount = retryCount,
        lastSyncAttempt = lastSyncAttempt
)

fun MacroTargetEntity.toDomain() =
    MacroTarget(
    id = id,
    targets = targets,
    bmr = bmr,
    tdee = tdee,
        bmi = bmi,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncStatus = syncStatus,
    retryCount = retryCount,
    lastSyncAttempt = lastSyncAttempt
)

fun MacroTargetDto.toEntity(
    syncStatus: SyncStatus = SyncStatus.SYNCED,
    lastSyncAttempt: Long? = System.currentTimeMillis()
) =
    MacroTargetEntity(
        id = id,
        targets = targets,
        bmr = bmr,
        tdee = tdee,
        bmi = bmi,
        createdAt = createdAt,
        updatedAt = updatedAt,
        syncStatus = syncStatus,
        retryCount = 0,
        lastSyncAttempt = lastSyncAttempt
    )

fun MacroTargetEntity.toFirestoreDto() =
    MacroTargetDto(
        id = id,
        targets = targets,
        bmr = bmr,
        tdee = tdee,
        bmi = bmi,
        createdAt = createdAt,
        updatedAt = updatedAt
    )