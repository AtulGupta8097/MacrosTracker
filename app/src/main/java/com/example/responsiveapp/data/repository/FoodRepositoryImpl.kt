package com.example.responsiveapp.data.repository

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
    private val fatSecretApi: FatSecretApiService,
) : FoodRepository {

    override suspend fun searchFoods(
        query: String,
        limit: Int
    ): Result<List<Food>> {

        return try {

            val cachedFoods = foodDao.searchFoods(query.lowercase(), limit)
            if (cachedFoods.isNotEmpty()) {
                return Result.success(cachedFoods.map { it.toDomain() })
            }

            val apiResponse = fatSecretApi.searchFoods(
                searchExpression = query,
                maxResults = limit
            )

            if (!apiResponse.isSuccessful) {
                return Result.failure(
                    Exception("FatSecret API error: ${apiResponse.code()} - ${apiResponse.message()}")
                )
            }

            val searchResult = apiResponse.body()
            val foodItems = searchResult?.foods?.food

            if (foodItems.isNullOrEmpty()) {
                return Result.success(emptyList())
            }

            val foods = foodItems.map { it.toDomain() }
            foodDao.insertFoods(foods.map { it.toEntity("fatsecret") })

            Result.success(foods)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFoodById(foodId: String): Result<Food> {

        return try {
            val cachedFood = foodDao.getFoodById(foodId)
            if (cachedFood != null && cachedFood.servings.isNotEmpty()) {
                return Result.success(cachedFood.toDomain())
            }

            val apiResponse = fatSecretApi.getFoodById(foodId = foodId)

            if (!apiResponse.isSuccessful) {
                return Result.failure(
                    Exception("Food not found: ${apiResponse.code()}")
                )
            }

            val foodDetail = apiResponse.body()?.food
                ?: return Result.failure(Exception("Food not found"))

            val food = foodDetail.toDomain()
            foodDao.insertFood(food.toEntity("fatsecret"))

            Result.success(food)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}