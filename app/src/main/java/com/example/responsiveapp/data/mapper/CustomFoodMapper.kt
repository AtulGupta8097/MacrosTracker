package com.example.responsiveapp.data.mapper

import com.example.responsiveapp.data.local.entity.CustomFoodEntity
import com.example.responsiveapp.data.remote.dto.firebase.CustomFoodDto
import com.example.responsiveapp.domain.model.SyncStatus
import com.example.responsiveapp.domain.model.myfood.CustomFood

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

