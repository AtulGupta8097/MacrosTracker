package com.macrivo.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.macrivo.app.data.local.entity.DailyMealCount
import com.macrivo.app.data.local.entity.FoodLogEntity
import com.macrivo.app.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodLogDao {

    @Query("""
        SELECT *
        FROM food_logs
        WHERE date = :date
        AND syncStatus != 'PENDING_DELETE'
        ORDER BY createdAt DESC
    """)
    fun getFoodLogsForDate(date: Long): Flow<List<FoodLogEntity>>

    @Query("""
        SELECT date, COUNT(*) as count
        FROM food_logs
        WHERE date BETWEEN :startDate AND :endDate
        AND syncStatus != 'PENDING_DELETE'
        GROUP BY date
    """)
    fun observeMealCountsBetweenDates(
        startDate: Long,
        endDate: Long,
    ): Flow<List<DailyMealCount>>

    @Query("""
        SELECT DISTINCT date
        FROM food_logs
        WHERE syncStatus != 'PENDING_DELETE'
        ORDER BY date DESC
    """)
    fun observeLoggedDates(): Flow<List<Long>>

    @Query("""
        SELECT *
        FROM food_logs
        WHERE id = :logId
    """)
    suspend fun getFoodLogById(logId: String): FoodLogEntity?

    @Query("""
        SELECT *
        FROM food_logs
        WHERE syncStatus = 'PENDING_DELETE'
        ORDER BY updatedAt ASC
    """)
    suspend fun getPendingDeletes(): List<FoodLogEntity>

    @Query("""
        SELECT id
        FROM food_logs
        WHERE syncStatus IN ('PENDING', 'FAILED', 'PENDING_DELETE')
    """)
    suspend fun getUnsyncedIds(): List<String>

    @Query("""
        UPDATE food_logs
        SET
            syncStatus = 'PENDING_DELETE',
            updatedAt = :updatedAt,
            retryCount = 0,
            lastSyncAttempt = NULL
        WHERE id = :logId
    """)
    suspend fun markPendingDelete(logId: String, updatedAt: Long)

    @Query("""
        SELECT *
        FROM food_logs
        WHERE syncStatus IN ('PENDING', 'FAILED')
        ORDER BY createdAt ASC
    """)
    suspend fun getPending(): List<FoodLogEntity>

    @Query("""
        SELECT COUNT(*)
        FROM food_logs
        WHERE syncStatus = 'PENDING'
    """)
    suspend fun getPendingCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodLog(foodLog: FoodLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFromRemote(foodLogs: List<FoodLogEntity>)

    @Update
    suspend fun updateFoodLog(foodLog: FoodLogEntity)

    @Query("""
        UPDATE food_logs
        SET
            syncStatus = :status,
            lastSyncAttempt = :lastSyncAttempt
        WHERE id = :logId
        AND syncStatus != 'PENDING_DELETE'
    """)
    suspend fun updateSyncStatus(
        logId: String,
        status: SyncStatus,
        lastSyncAttempt: Long
    )

    @Query("""
        UPDATE food_logs
        SET
            retryCount = :retryCount,
            lastSyncAttempt = :lastSyncAttempt
        WHERE id = :logId
    """)
    suspend fun updateRetryInfo(
        logId: String,
        retryCount: Int,
        lastSyncAttempt: Long
    )

    @Query("""
        DELETE FROM food_logs
        WHERE id = :logId
    """)
    suspend fun deleteFoodLog(logId: String)

    @Query("""
        DELETE FROM food_logs
    """)
    suspend fun deleteAllLogs()
}