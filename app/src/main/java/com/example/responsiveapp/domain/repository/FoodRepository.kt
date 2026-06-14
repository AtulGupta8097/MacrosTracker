package com.example.responsiveapp.domain.repository

import com.example.responsiveapp.core.utils.Resource
import com.example.responsiveapp.domain.model.FoodDetail
import com.example.responsiveapp.domain.model.FoodItem
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    fun searchFoods(
        query: String,
        limit: Int = 20,
    ): Flow<Resource<List<FoodItem>>>

    suspend fun getFoodDetail(
        foodId: String,
    ): Result<FoodDetail>
}