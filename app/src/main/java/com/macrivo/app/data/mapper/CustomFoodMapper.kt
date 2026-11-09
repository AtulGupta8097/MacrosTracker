package com.macrivo.app.data.mapper

import com.macrivo.app.data.local.entity.CustomFoodEntity
import com.macrivo.app.data.remote.dto.firebase.CustomFoodDto
import com.macrivo.app.domain.model.SyncStatus
import com.macrivo.app.domain.model.myfood.CustomFood

fun CustomFoodEntity.toDomain() = CustomFood(
    id = id,
    name = name,
    description = description,
    servingSize = servingSize,
    servingsPerContainer = servingsPerContainer,
    nutrition = nutrition,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun CustomFood.toEntity() = CustomFoodEntity(
    id = id,
    name = name,
    description = description,
    servingSize = servingSize,
    servingsPerContainer = servingsPerContainer,
    nutrition = nutrition,
    updatedAt = updatedAt,
    createdAt = createdAt,
)

fun CustomFoodDto.toEntity(
    syncStatus: SyncStatus = SyncStatus.SYNCED,
    lastSyncAttempt: Long? = System.currentTimeMillis()
) = CustomFoodEntity(
    id = id,
    name = name,
    description = description,
    servingSize = servingSize,
    servingsPerContainer = servingsPerContainer,
    nutrition = nutrition,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncStatus = syncStatus,
    retryCount = 0,
    lastSyncAttempt = lastSyncAttempt
)

fun CustomFoodEntity.toFirestoreDto() =
    CustomFoodDto(
        id = id,
        name = name,
        description = description,
        servingSize = servingSize,
        servingsPerContainer = servingsPerContainer,
        nutrition = nutrition,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

