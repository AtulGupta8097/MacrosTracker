package com.example.responsiveapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.responsiveapp.data.local.entity.FoodDetailEntity

@Dao
interface FoodDetailDao {

    @Query("SELECT * FROM food_details WHERE id = :foodId")
    suspend fun getById(foodId: String): FoodDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: FoodDetailEntity)

    @Query("DELETE FROM food_details WHERE cachedAt < :expireTime")
    suspend fun deleteExpired(expireTime: Long)
}