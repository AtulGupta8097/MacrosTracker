package com.example.responsiveapp.data.repository

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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
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
        rawQuery: String,
        limit: Int,
    ): Flow<Resource<List<FoodItem>>> = channelFlow {

        val normalizedQuery = rawQuery.trim().lowercase()

        val roomJob = launch {
            foodSearchDao
                .observeSearchResults(normalizedQuery)
                .distinctUntilChanged()
                .collect { foods ->
                    if (foods.isNotEmpty()) {
                        send(
                            Resource.Success(
                                foods.map { it.toDomain() },
                            ),
                        )
                    }
                }
        }

        val searchLock =
            searchLocks.getOrPut(normalizedQuery) { Mutex() }

        val shouldFetch = searchLock.withLock {
            val cachedQuery =
                foodSearchDao.getSearchQuery(normalizedQuery)

            cachedQuery == null ||
                    !cachedQuery.canServe(limit)
        }

        if (shouldFetch) {

            val hasCachedResults =
                foodSearchDao
                    .getSearchResults(normalizedQuery)
                    .isNotEmpty()

            if (!hasCachedResults) {
                send(Resource.Loading())
            }

            runCatching {
                api.searchFoods(
                    searchExpression = rawQuery,
                    maxResults = limit,
                )
            }.fold(
                onSuccess = { response ->

                    if (!response.isSuccessful) {
                        if (!hasCachedResults) {
                            send(
                                Resource.Error(
                                    "API error: ${response.code()}",
                                ),
                            )
                        }
                        return@fold
                    }

                    val foodDtos =
                        response.body()?.foods?.food.orEmpty()

                    val totalResults =
                        response.body()
                            ?.foods
                            ?.totalResults
                            ?.toIntOrNull()
                            ?: foodDtos.size

                    val domainFoods =
                        foodDtos.map { it.toDomain() }

                    val foodEntities =
                        domainFoods.map { it.toEntity() }

                    val searchMappings =
                        foodEntities.mapIndexed { position, food ->
                            SearchResultCrossRef(
                                query = normalizedQuery,
                                foodId = food.id,
                                position = position,
                            )
                        }

                    val searchQueryEntity =
                        SearchQueryEntity(
                            query = normalizedQuery,
                            totalResults = totalResults,
                            cachedCount = foodEntities.size,
                            isComplete =
                                foodEntities.size >= totalResults ||
                                        foodEntities.size >= limit,
                            lastFetchedAt = System.currentTimeMillis(),
                        )

                    foodSearchDao.replaceSearchResults(
                        searchQuery = searchQueryEntity,
                        foods = foodEntities,
                        results = searchMappings,
                    )

                    launch {
                        foodSearchDao.deleteOldQueries(
                            MAX_CACHED_QUERIES,
                        )
                        foodSearchDao.deleteOrphanedFoods()
                    }
                },
                onFailure = { throwable ->
                    if (!hasCachedResults) {
                        send(
                            Resource.Error(
                                throwable.message
                                    ?: "Network error",
                            ),
                        )
                    }
                },
            )
        }

        awaitClose {
            roomJob.cancel()
        }
    }.flowOn(ioDispatcher)

    override suspend fun getFoodDetail(
        foodId: String,
    ): Result<FoodDetail> = withContext(ioDispatcher) {

        runCatching {

            val cachedDetail =
                foodDetailDao.getById(foodId)

            if (
                cachedDetail != null &&
                cachedDetail.servings.isNotEmpty()
            ) {
                return@runCatching cachedDetail.toDomain()
            }

            val apiResponse =
                api.getFoodById(foodId)

            if (!apiResponse.isSuccessful) {
                error("Food not found: ${apiResponse.code()}")
            }

            val foodDto =
                apiResponse.body()?.food
                    ?: error(
                        "Empty response body for food $foodId",
                    )

            val foodDetail =
                foodDto.toDomain()

            runCatching {
                foodDetailDao.insert(
                    foodDetail.toEntity(),
                )
            }

            foodDetail
        }
    }
}