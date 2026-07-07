package com.example.responsiveapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.responsiveapp.data.local.entity.DailySummaryEntity
import com.example.responsiveapp.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface DailySummaryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: DailySummaryEntity)

    @Update
    suspend fun update(entity: DailySummaryEntity)

    @Query("SELECT * FROM daily_summaries WHERE date = :startOfDay")
    fun observeForDate(startOfDay: Long): Flow<DailySummaryEntity?>

    @Query("SELECT * FROM daily_summaries WHERE date = :startOfDay")
    suspend fun getForDate(startOfDay: Long): DailySummaryEntity?

    @Query(
        """
        SELECT * FROM daily_summaries
        WHERE syncStatus IN ('PENDING', 'FAILED')
        ORDER BY date ASC
        """
    )
    suspend fun getPending(): List<DailySummaryEntity>

    @Query(
        """
        UPDATE daily_summaries
        SET syncStatus = :status,
            lastSyncAttempt = :lastSyncAttempt
        WHERE date = :date
        """
    )
    suspend fun updateSyncStatus(
        date: Long,
        status: SyncStatus,
        lastSyncAttempt: Long
    )

    @Query(
        """
        UPDATE daily_summaries
        SET retryCount = :retryCount,
            lastSyncAttempt = :lastSyncAttempt
        WHERE date = :date
        """
    )
    suspend fun updateRetryInfo(
        date: Long,
        retryCount: Int,
        lastSyncAttempt: Long
    )

    @Query("DELETE FROM daily_summaries")
    suspend fun deleteAll()
}