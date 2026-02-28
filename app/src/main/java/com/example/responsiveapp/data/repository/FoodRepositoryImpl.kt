package com.example.responsiveapp.data.repository

import android.util.Log
import com.example.responsiveapp.data.local.dao.FoodDetailDao
import com.example.responsiveapp.data.local.dao.FoodItemDao
import com.example.responsiveapp.data.mapper.toDomain
import com.example.responsiveapp.data.mapper.toEntity
import com.example.responsiveapp.data.remote.api.FatSecretApiService
import com.example.responsiveapp.domain.model.FoodDetail
import com.example.responsiveapp.domain.model.FoodItem
import com.example.responsiveapp.domain.repository.FoodRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class FoodRepositoryImpl @Inject constructor(
    private val foodItemDao: FoodItemDao,
    private val foodDetailDao: FoodDetailDao,
    private val api: FatSecretApiService,
    private val ioDispatcher: CoroutineDispatcher
) : FoodRepository {

    override suspend fun searchFoods(query: String, limit: Int): Result<List<FoodItem>> =
        withContext(ioDispatcher) {
            try {
                val cached = foodItemDao.searchFoods(query.lowercase(), limit)
                if (cached.isNotEmpty()) {
                    return@withContext Result.success(cached.map { it.toDomain() })
                }

                val response = api.searchFoods(query, maxResults = limit)
                if (!response.isSuccessful) {
                    return@withContext Result.failure(
                        Exception("API error ${response.code()} ${response.message()}")
                    )
                }

                val items = response.body()?.foods?.food.orEmpty()
                    .map { it.toDomain() }
                    .filter { (it.macroSummary?.calories?.roundToInt() ?: 0) > 0 }

                runCatching { foodItemDao.insertAll(items.map { it.toEntity() }) }

                Result.success(items)
            } catch (e: Exception) {
                Log.e("FoodRepository", "Search error: ${e.message}")
                Result.failure(e)
            }
        }

    override suspend fun getFoodDetail(foodId: String): Result<FoodDetail> =
        withContext(ioDispatcher) {
            try {
                val cached = foodDetailDao.getById(foodId)
                if (cached != null && cached.servings.isNotEmpty()) {
                    return@withContext Result.success(cached.toDomain())
                }

                val response = api.getFoodById(foodId)
                if (!response.isSuccessful) {
                    return@withContext Result.failure(
                        Exception("Food not found ${response.code()}")
                    )
                }

                val detail = response.body()?.food
                    ?: return@withContext Result.failure(Exception("Food not found"))

                val domainDetail = detail.toDomain()
                runCatching { foodDetailDao.insert(domainDetail.toEntity()) }

                Result.success(domainDetail)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}