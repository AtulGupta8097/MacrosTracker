package com.example.responsiveapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.responsiveapp.data.local.entity.FoodItemEntity

@Dao
interface FoodItemDao {

    @Query("SELECT * FROM food_items WHERE name LIKE '%' || :query || '%' LIMIT :limit")
    suspend fun searchFoods(query: String, limit: Int): List<FoodItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(foods: List<FoodItemEntity>)

    @Query("DELETE FROM food_items WHERE cachedAt < :expireTime")
    suspend fun deleteExpired(expireTime: Long)

    @Query("SELECT COUNT(*) FROM food_items")
    suspend fun count(): Int
}