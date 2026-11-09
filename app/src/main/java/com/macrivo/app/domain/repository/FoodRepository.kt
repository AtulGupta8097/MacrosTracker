package com.macrivo.app.domain.repository

import com.macrivo.app.core.utils.Resource
import com.macrivo.app.domain.model.FoodDetail
import com.macrivo.app.domain.model.FoodItem
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    fun searchFoods(
        query: String,
        limit: Int = 20,
    ): Flow<Resource<List<FoodItem>>>

    suspend fun getFoodDetail(
        foodId: String,
    ): Result<FoodDetail>

    suspend fun getFoodDetailByBarcode(
        barcode: String,
    ): Result<FoodDetail>
}