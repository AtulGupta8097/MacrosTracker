package com.example.responsiveapp.data.mapper

import com.example.responsiveapp.data.local.entity.MacroTargetEntity
import com.example.responsiveapp.domain.model.macros.MacroTarget

fun MacroTarget.toEntity() =
    MacroTargetEntity(
        id            = id,
        targets       = targets,
        bmr           = bmr,
        tdee          = tdee,
        createdAt     = createdAt,
        syncStatus    = syncStatus,
        retryCount    = retryCount,
        lastSyncAttempt = lastSyncAttempt
    )

fun MacroTargetEntity.toDomain() =
    MacroTarget(
        id            = id,
        targets       = targets,
        bmr           = bmr,
        tdee          = tdee,
        createdAt     = createdAt,
        syncStatus    = syncStatus,
        retryCount    = retryCount,
        lastSyncAttempt = lastSyncAttempt
    )