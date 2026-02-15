package com.example.responsiveapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.responsiveapp.data.local.entity.FoodEntity

@Dao
interface FoodDao {

    @Query("SELECT * FROM foods WHERE id = :foodId")
    suspend fun getFoodById(foodId: String): FoodEntity?

    @Query("SELECT * FROM foods WHERE searchableName LIKE '%' || :query || '%' LIMIT :limit")
    suspend fun searchFoods(query: String, limit: Int): List<FoodEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: FoodEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(foods: List<FoodEntity>)

    @Query("DELETE FROM foods WHERE cachedAt < :expireTime")
    suspend fun deleteExpiredFoods(expireTime: Long)

    @Query("SELECT COUNT(*) FROM foods")
    suspend fun getFoodCount(): Int
}