package com.example.responsiveapp.data.repository

import android.util.Log
import com.example.responsiveapp.data.local.dao.FoodDao
import com.example.responsiveapp.data.mapper.toDomain
import com.example.responsiveapp.data.mapper.toEntity
import com.example.responsiveapp.data.remote.api.FatSecretApiService
import com.example.responsiveapp.domain.model.Food
import com.example.responsiveapp.domain.repository.FoodRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepositoryImpl @Inject constructor(
    private val foodDao: FoodDao,
    private val api: FatSecretApiService
) : FoodRepository {

    override suspend fun searchFoods(query: String, limit: Int): Result<List<Food>> {
        return try {

            val cached = foodDao.searchFoods(query.lowercase(), limit)

            if (cached.isNotEmpty()) {
                return Result.success(cached.map { it.toDomain() })
            }

            val response = api.searchFoods(searchExpression = query, maxResults = limit)
            if (!response.isSuccessful) {
                return Result.failure(Exception("API error ${response.code()} ${response.message()}"))
            }

            val foodsDto = response.body()?.foods?.food.orEmpty()
            val foods = foodsDto.map { it.toDomain() }

            try {
                foodDao.insertFoods(foods.map { it.toEntity("fatsecret") })
            } catch (e: Exception) {
                Log.e("FoodRepository", "Cache write failed: ${e.message}")
            }

            Result.success(foods)

        } catch (e: Exception) {
            Log.e("FoodRepository", "API error: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getFoodById(foodId: String): Result<Food> {
        return try {

            val cached = foodDao.getFoodById(foodId)
            if (cached != null && cached.servings.isNotEmpty()) {
                return Result.success(cached.toDomain())
            }

            val response = api.getFoodById(foodId = foodId)

            if (!response.isSuccessful) {
                return Result.failure(Exception("Food not found ${response.code()}"))
            }

            val foodDto = response.body()?.food
                ?: return Result.failure(Exception("Food not found"))

            val food = foodDto.toDomain()

            foodDao.insertFood(food.toEntity("fatsecret"))

            Result.success(food)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
