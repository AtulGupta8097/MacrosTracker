package com.macrivo.app.data.mapper

import com.macrivo.app.data.local.converter.ServingConverter
import com.macrivo.app.data.local.entity.SavedFoodEntity
import com.macrivo.app.data.remote.dto.firebase.SavedFoodDto
import com.macrivo.app.domain.model.FoodDetail
import com.macrivo.app.domain.model.SyncStatus

private val servingConverter = ServingConverter()

fun FoodDetail.toSavedEntity(
    savedAt: Long = System.currentTimeMillis(),
): SavedFoodEntity = SavedFoodEntity(
    id = id,
    name = name,
    brand = brand,
    foodType = foodType,
    imageUrl = imageUrl,
    barcode = barcode,
    isVerified = isVerified,
    servings = servings.map { it.toEntity() },
    savedAt = savedAt,
)

fun SavedFoodEntity.toDomain(): FoodDetail = FoodDetail(
    id = id,
    name = name,
    brand = brand,
    foodType = foodType,
    imageUrl = imageUrl,
    barcode = barcode,
    isVerified = isVerified,
    servings = servings.map { it.toDomain() },
    cachedAt = savedAt,
)

fun SavedFoodEntity.toFirestoreDto(): SavedFoodDto = SavedFoodDto(
    id = id,
    name = name,
    brand = brand,
    foodType = foodType,
    imageUrl = imageUrl,
    barcode = barcode,
    verified = isVerified,
    servingsJson = servingConverter.fromServingList(servings),
    savedAt = savedAt,
)

fun SavedFoodDto.toEntity(
    syncStatus: SyncStatus = SyncStatus.SYNCED,
    lastSyncAttempt: Long? = System.currentTimeMillis(),
): SavedFoodEntity = SavedFoodEntity(
    id = id,
    name = name,
    brand = brand,
    foodType = foodType,
    imageUrl = imageUrl,
    barcode = barcode,
    isVerified = verified,
    servings = servingConverter.toServingList(servingsJson),
    savedAt = savedAt,
    syncStatus = syncStatus,
    retryCount = 0,
    lastSyncAttempt = lastSyncAttempt,
)
