package com.example.responsiveapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.responsiveapp.data.local.entity.FoodLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodLogDao {

    @Query("SELECT * FROM food_logs WHERE userId = :userId AND date = :date ORDER BY createdAt DESC")
    fun getFoodLogsForDate(userId: String, date: Long): Flow<List<FoodLogEntity>>

    @Query("SELECT * FROM food_logs WHERE id = :logId")
    suspend fun getFoodLogById(logId: String): FoodLogEntity?

    @Query("SELECT * FROM food_logs WHERE userId = :userId AND syncStatus = 'PENDING'")
    suspend fun getPendingLogs(userId: String): List<FoodLogEntity>

    @Query("SELECT COUNT(*) FROM food_logs WHERE userId = :userId AND syncStatus = 'PENDING'")
    suspend fun getPendingLogsCount(userId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodLog(foodLog: FoodLogEntity)

    @Update
    suspend fun updateFoodLog(foodLog: FoodLogEntity)

    @Query("UPDATE food_logs SET syncStatus = :status WHERE id = :logId")
    suspend fun updateSyncStatus(logId: String, status: String)

    @Query("DELETE FROM food_logs WHERE id = :logId")
    suspend fun deleteFoodLog(logId: String)

    @Query("DELETE FROM food_logs WHERE userId = :userId")
    suspend fun deleteAllUserLogs(userId: String)
}