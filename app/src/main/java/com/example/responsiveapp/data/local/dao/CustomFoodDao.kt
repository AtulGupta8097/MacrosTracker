package com.example.responsiveapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.responsiveapp.data.local.entity.CustomFoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomFoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(food: CustomFoodEntity)

    @Query("SELECT * FROM custom_foods ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<CustomFoodEntity>>

    @Query("DELETE FROM custom_foods WHERE id = :id")
    suspend fun deleteById(id: String)
}