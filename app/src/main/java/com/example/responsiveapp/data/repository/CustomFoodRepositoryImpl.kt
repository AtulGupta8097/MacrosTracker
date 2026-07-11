package com.example.responsiveapp.data.repository

import com.example.responsiveapp.data.local.dao.CustomFoodDao
import com.example.responsiveapp.data.mapper.toDomain
import com.example.responsiveapp.data.mapper.toEntity
import com.example.responsiveapp.domain.model.myfood.CustomFood
import com.example.responsiveapp.domain.repository.CustomFoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CustomFoodRepositoryImpl @Inject constructor(
    private val dao: CustomFoodDao,
) : CustomFoodRepository {

    override fun observeAll(): Flow<List<CustomFood>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun save(food: CustomFood) = dao.upsert(food.toEntity())

    override suspend fun delete(foodId: String) = dao.deleteById(foodId)

    override suspend fun syncPending() {
        TODO("Not yet implemented")
    }

}