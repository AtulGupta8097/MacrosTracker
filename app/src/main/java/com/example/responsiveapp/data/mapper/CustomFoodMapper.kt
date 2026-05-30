package com.example.responsiveapp.data.mapper

import com.example.responsiveapp.data.local.entity.CustomFoodEntity
import com.example.responsiveapp.domain.model.myfood.CustomFood

fun CustomFoodEntity.toDomain() = CustomFood(
    id = id,
    name = name,
    description = description,
    servingSize = servingSize,
    servingsPerContainer = servingsPerContainer,
    nutrition = nutrition,
    createdAt = createdAt,
)

fun CustomFood.toEntity() = CustomFoodEntity(
    id = id,
    name = name,
    description = description,
    servingSize = servingSize,
    servingsPerContainer = servingsPerContainer,
    nutrition = nutrition,
    createdAt = createdAt,
)