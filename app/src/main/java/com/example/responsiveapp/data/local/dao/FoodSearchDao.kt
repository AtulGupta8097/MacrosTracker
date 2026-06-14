package com.example.responsiveapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.responsiveapp.data.local.entity.FoodItemEntity
import com.example.responsiveapp.data.local.entity.SearchQueryEntity
import com.example.responsiveapp.data.local.entity.SearchResultCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodSearchDao {

    @Query(
        """
        SELECT f.*
        FROM food_items f
        INNER JOIN search_result_cross_ref r
            ON f.id = r.foodId
        WHERE r.query = :query
        ORDER BY r.position
        """
    )
    fun observeSearchResults(
        query: String,
    ): Flow<List<FoodItemEntity>>

    @Query(
        """
        SELECT f.*
        FROM food_items f
        INNER JOIN search_result_cross_ref r
            ON f.id = r.foodId
        WHERE r.query = :query
        ORDER BY r.position
        """
    )
    suspend fun getSearchResults(
        query: String,
    ): List<FoodItemEntity>

    @Query(
        """
        SELECT *
        FROM search_queries
        WHERE query = :query
        """
    )
    suspend fun getSearchQuery(
        query: String,
    ): SearchQueryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceFoods(
        foods: List<FoodItemEntity>,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceSearchQuery(
        searchQuery: SearchQueryEntity,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceSearchResults(
        results: List<SearchResultCrossRef>,
    )

    @Query(
        """
        DELETE FROM search_result_cross_ref
        WHERE query = :query
        """
    )
    suspend fun deleteSearchResults(
        query: String,
    )

    @Query(
        """
        DELETE FROM search_queries
        WHERE query NOT IN (
            SELECT query
            FROM search_queries
            ORDER BY lastFetchedAt DESC
            LIMIT :maxQueries
        )
        """
    )
    suspend fun deleteOldQueries(
        maxQueries: Int,
    )

    @Query(
        """
        DELETE FROM food_items
        WHERE id NOT IN (
            SELECT DISTINCT foodId
            FROM search_result_cross_ref
        )
        """
    )
    suspend fun deleteOrphanedFoods()

    @Transaction
    suspend fun replaceSearchResults(
        searchQuery: SearchQueryEntity,
        foods: List<FoodItemEntity>,
        results: List<SearchResultCrossRef>,
    ) {
        deleteSearchResults(searchQuery.query)
        insertOrReplaceFoods(foods)
        insertOrReplaceSearchQuery(searchQuery)
        insertOrReplaceSearchResults(results)
    }
}