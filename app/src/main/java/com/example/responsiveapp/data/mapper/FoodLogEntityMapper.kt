package com.example.responsiveapp.data.mapper

import com.example.responsiveapp.data.local.entity.FoodLogEntity
import com.example.responsiveapp.data.remote.dto.firebase.FoodLogDto
import com.example.responsiveapp.domain.model.FoodLog
import com.example.responsiveapp.domain.model.MealIngredient
import com.example.responsiveapp.domain.model.SyncStatus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun FoodLogEntity.toDomain(): FoodLog {

    val ingredients: List<MealIngredient> =
        ingredientsJson?.let {
            val type = object : TypeToken<List<MealIngredient>>() {}.type
            Gson().fromJson(it, type)
        } ?: emptyList()

    return FoodLog(
        id                  = id,
        date                = date,
        foodName            = foodName,
        servingDescription  = servingDescription,
        quantity            = quantity,
        nutrition           = nutrition,
        ingredients         = ingredients,
        createdAt           = createdAt,
        updatedAt           = updatedAt,
        syncStatus          = syncStatus,
        retryCount          = retryCount,
        lastSyncAttempt     = lastSyncAttempt
    )
}

fun FoodLog.toEntity(): FoodLogEntity {

    return FoodLogEntity(
        id                  = id,
        date                = date,
        foodName            = foodName,
        servingDescription  = servingDescription,
        quantity            = quantity,
        nutrition           = nutrition,
        ingredientsJson     = if (ingredients.isNotEmpty()) Gson().toJson(ingredients) else null,
        createdAt           = createdAt,
        updatedAt           = updatedAt,
        syncStatus          = SyncStatus.PENDING,
        retryCount          = 0,
        lastSyncAttempt     = null
    )
}

fun FoodLogEntity.toFirestoreDto(): FoodLogDto {

    val ingredients: List<MealIngredient> =
        ingredientsJson?.let {
            val type = object : TypeToken<List<MealIngredient>>() {}.type
            Gson().fromJson(it, type)
        } ?: emptyList()

    return FoodLogDto(
        id = id,
        date = date,
        foodName = foodName,
        servingDescription = servingDescription,
        quantity = quantity,
        nutrition = nutrition,
        ingredients = ingredients,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}