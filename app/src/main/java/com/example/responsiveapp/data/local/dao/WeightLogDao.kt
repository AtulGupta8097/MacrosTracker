package com.example.responsiveapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.responsiveapp.data.local.entity.WeightLogEntity
import com.example.responsiveapp.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeight(weightLog: WeightLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFromRemote(weightLogs: List<WeightLogEntity>)

    @Update
    suspend fun updateWeight(weightLog: WeightLogEntity)

    @Query("""
        DELETE FROM weight_logs
        WHERE id = :id
    """)
    suspend fun deleteWeight(id: String)

    @Query("""
        SELECT *
        FROM weight_logs
        ORDER BY date DESC, createdAt DESC
        LIMIT 1
    """)
    fun observeLatestWeight(): Flow<WeightLogEntity?>

    @Query("""
        SELECT *
        FROM weight_logs
        ORDER BY date DESC, createdAt DESC
    """)
    fun observeWeightHistory(): Flow<List<WeightLogEntity>>

    @Query("""
        SELECT *
        FROM weight_logs
        WHERE date BETWEEN :startDate AND :endDate
        ORDER BY date ASC
    """)
    fun observeWeightBetweenDates(
        startDate: Long,
        endDate: Long,
    ): Flow<List<WeightLogEntity>>

    @Query("""
        SELECT *
        FROM weight_logs
        WHERE syncStatus IN ('PENDING', 'FAILED')
        ORDER BY createdAt ASC
    """)
    suspend fun getPending(): List<WeightLogEntity>

    @Query("""
        UPDATE weight_logs
        SET
            syncStatus = :status,
            lastSyncAttempt = :lastSyncAttempt
        WHERE id = :id
    """)
    suspend fun updateSyncStatus(
        id: String,
        status: SyncStatus,
        lastSyncAttempt: Long,
    )

    @Query("""
        UPDATE weight_logs
        SET
            retryCount = :retryCount,
            lastSyncAttempt = :lastSyncAttempt
        WHERE id = :id
    """)
    suspend fun updateRetryInfo(
        id: String,
        retryCount: Int,
        lastSyncAttempt: Long,
    )
}
