package com.example.responsiveapp.data.repository

import android.util.Log
import com.example.responsiveapp.core.di.IoDispatcher
import com.example.responsiveapp.core.utils.Resource
import com.example.responsiveapp.data.local.CachePolicy.MAX_CACHED_QUERIES
import com.example.responsiveapp.data.local.CachePolicy.canServe
import com.example.responsiveapp.data.local.dao.FoodDetailDao
import com.example.responsiveapp.data.local.dao.FoodSearchDao
import com.example.responsiveapp.data.local.entity.SearchQueryEntity
import com.example.responsiveapp.data.local.entity.SearchResultCrossRef
import com.example.responsiveapp.data.mapper.toDomain
import com.example.responsiveapp.data.mapper.toEntity
import com.example.responsiveapp.data.remote.api.FatSecretApiService
import com.example.responsiveapp.domain.model.FoodDetail
import com.example.responsiveapp.domain.model.FoodItem
import com.example.responsiveapp.domain.repository.FoodRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepositoryImpl @Inject constructor(
    private val foodSearchDao: FoodSearchDao,
    private val foodDetailDao: FoodDetailDao,
    private val api: FatSecretApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : FoodRepository {

    private val searchLocks = ConcurrentHashMap<String, Mutex>()

    override fun searchFoods(
        query: String,
        limit: Int,
    ): Flow<Resource<List<FoodItem>>> = flow {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isBlank()) {
            emit(Resource.Success<List<FoodItem>>(emptyList()))
            return@flow
        }

        val normalizedQuery = trimmedQuery.lowercase(Locale.ROOT)
        // Bump this namespace after changing response/cache validation so old
        // empty entries created by the permissive parser are fetched again.
        val cacheKey = "$SEARCH_CACHE_VERSION:$normalizedQuery"
        val requestedLimit = limit.coerceAtLeast(1)
        val searchLock = searchLocks.getOrPut(cacheKey) { Mutex() }

        searchLock.withLock {
            val snapshot = foodSearchDao.getSearchCacheSnapshot(cacheKey)
            val cachedQuery = snapshot.searchQuery
            val cachedEntities = snapshot.foods

            // Metadata is only trusted when its count and completion flag agree
            // with the rows actually returned by Room.
            val cacheIsConsistent = cachedQuery != null &&
                    cachedQuery.totalResults >= 0 &&
                    cachedQuery.cachedCount >= 0 &&
                    cachedQuery.cachedCount == cachedEntities.size &&
                    cachedQuery.cachedCount <= cachedQuery.totalResults &&
                    cachedQuery.isComplete ==
                    (cachedQuery.cachedCount >= cachedQuery.totalResults)

            // Joined rows can still be displayed as a stale preview while a
            // refresh runs, even when their metadata is inconsistent. The
            // consistency check below controls whether they can satisfy cache.
            val cachedFoods = cachedEntities.map { it.toDomain() }

            val validEmptyCache = cacheIsConsistent &&
                    cachedQuery != null &&
                    cachedFoods.isEmpty() &&
                    cachedQuery.totalResults == 0 &&
                    cachedQuery.isComplete

            val hasCachedPreview = cachedFoods.isNotEmpty() || validEmptyCache

            // Emit stale rows first; the ViewModel keeps them visible when the
            // following Loading state arrives.
            if (hasCachedPreview) {
                emit(Resource.Success(cachedFoods))
            }

            val canUseCache = cacheIsConsistent &&
                    cachedQuery?.canServe(requestedLimit) == true
            if (canUseCache) {
                return@withLock
            }

            emit(Resource.Loading())

            val response = try {
                api.searchFoods(
                    searchExpression = trimmedQuery,
                    maxResults = requestedLimit,
                )
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (exception: Exception) {
                emit(
                    Resource.Error(
                        exception.message ?: "Unable to search foods",
                    ),
                )
                return@withLock
            }

            if (!response.isSuccessful) {
                emit(Resource.Error("API error: ${response.code()}"))
                return@withLock
            }

            val body = response.body()
            if (body == null) {
                emit(Resource.Error("Empty API response"))
                return@withLock
            }

            val foodsContainer = body.foods
            if (foodsContainer == null) {
                Log.w(TAG, "Food search response did not include a foods container for $normalizedQuery")
                emit(Resource.Error("The food service returned an unexpected response. Please try again."))
                return@withLock
            }

            val foodDtos = foodsContainer.food.orEmpty()
            val reportedTotal = foodsContainer.totalResults
                ?.toIntOrNull()
                ?.takeIf { it >= 0 }

            // A legitimate empty search includes an explicit zero count. A
            // missing count or missing foods wrapper must not become a cached
            // empty success, because that can mask an API response error.
            if (foodDtos.isEmpty() && reportedTotal != 0) {
                Log.w(TAG, "Food search response had no foods and no zero total for $normalizedQuery")
                emit(Resource.Error("The food service returned an unexpected response. Please try again."))
                return@withLock
            }

            val savedFoods: List<FoodItem>
            try {
                val domainFoods = foodDtos
                    .map { it.toDomain() }
                    .distinctBy { it.id }

                val foodEntities = domainFoods.map { it.toEntity() }
                val totalResults = maxOf(
                    reportedTotal ?: foodEntities.size,
                    foodEntities.size,
                )

                val searchMappings = foodEntities.mapIndexed { position, food ->
                    SearchResultCrossRef(
                        query = cacheKey,
                        foodId = food.id,
                        position = position,
                    )
                }

                val searchQueryEntity = SearchQueryEntity(
                    query = cacheKey,
                    totalResults = totalResults,
                    cachedCount = foodEntities.size,
                    isComplete = foodEntities.size >= totalResults,
                    lastFetchedAt = System.currentTimeMillis(),
                )

                foodSearchDao.replaceSearchResults(
                    searchQuery = searchQueryEntity,
                    foods = foodEntities,
                    results = searchMappings,
                )
                savedFoods = domainFoods
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (exception: Exception) {
                emit(
                    Resource.Error(
                        exception.message ?: "Unable to save search results",
                    ),
                )
                return@withLock
            }

            // Cache cleanup must not turn a successful search into an error.
            try {
                foodSearchDao.deleteOldQueries(MAX_CACHED_QUERIES)
                foodSearchDao.deleteOrphanedFoods()
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (exception: Exception) {
                Log.w(TAG, "Unable to clean up old search cache entries", exception)
            }

            emit(Resource.Success(savedFoods))
        }
    }.flowOn(ioDispatcher)

    override suspend fun getFoodDetail(
        foodId: String,
    ): Result<FoodDetail> = withContext(ioDispatcher) {
        runCatching {
            val cachedDetail = foodDetailDao.getById(foodId)

            if (cachedDetail != null && cachedDetail.servings.isNotEmpty()) {
                return@runCatching cachedDetail.toDomain()
            }

            val apiResponse = api.getFoodById(foodId)
            if (!apiResponse.isSuccessful) {
                error("Food not found: ${apiResponse.code()}")
            }

            val foodDto = apiResponse.body()?.food
                ?: error("Empty response body for food $foodId")
            val foodDetail = foodDto.toDomain()

            runCatching {
                foodDetailDao.insert(foodDetail.toEntity())
            }

            foodDetail
        }
    }

    override suspend fun getFoodDetailByBarcode(
        barcode: String,
    ): Result<FoodDetail> = withContext(ioDispatcher) {
        runCatching {
            val lookupResponse = api.findFoodIdForBarcode(barcode)
            Log.d("TAG", "getFoodDetailByBarcode: $lookupResponse")

            if (!lookupResponse.isSuccessful) {
                error("Barcode lookup failed: ${lookupResponse.code()}")
            }

            val foodId = lookupResponse.body()?.foodId?.value
                ?: error("Empty barcode lookup response")

            if (foodId == "0") {
                throw NoSuchElementException(NO_BARCODE_MATCH)
            }

            getFoodDetail(foodId).getOrThrow()
        }
    }

    private companion object {
        const val TAG = "FoodRepositoryImpl"
        const val SEARCH_CACHE_VERSION = "food-search-v2"
        const val NO_BARCODE_MATCH = "no_barcode_match"
    }
}
