package com.example.responsiveapp.data.mapper

import com.example.responsiveapp.data.local.entity.MacroTargetEntity
import com.example.responsiveapp.data.remote.dto.firebase.MacroTargetDto
import com.example.responsiveapp.domain.model.SyncStatus
import com.example.responsiveapp.domain.model.macros.MacroTarget

fun MacroTarget.toEntity() =
    MacroTargetEntity(
    id = id,
    targets = targets,
    bmr = bmr,
    tdee = tdee,
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
        createdAt = createdAt,
        updatedAt = updatedAt
    )