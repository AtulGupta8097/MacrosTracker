package com.example.responsiveapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.responsiveapp.data.local.entity.MacroTargetEntity
import com.example.responsiveapp.domain.model.SyncStatus

@Dao
interface MacroTargetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(target: MacroTargetEntity)

    @Query("""
        SELECT * FROM macro_targets
        ORDER BY createdAt DESC
        LIMIT 1
    """)
    suspend fun getCurrentTarget(): MacroTargetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFromRemote(targets: List<MacroTargetEntity>)

    @Query("""
        SELECT * FROM macro_targets
        ORDER BY createdAt DESC
        LIMIT 1
    """)
    fun observeCurrentTarget(): kotlinx.coroutines.flow.Flow<MacroTargetEntity?>

    @Query("""
        SELECT * FROM macro_targets
        WHERE syncStatus IN ('PENDING', 'FAILED')
        ORDER BY createdAt ASC
    """)
    suspend fun getPending(): List<MacroTargetEntity>

    @Query("""
        UPDATE macro_targets
        SET syncStatus = :status,
            lastSyncAttempt = :lastSyncAttempt
        WHERE id = :id
    """)
    suspend fun updateSyncStatus(id: String, status: SyncStatus, lastSyncAttempt: Long)

    @Query("""
        UPDATE macro_targets
        SET retryCount = :retryCount,
            lastSyncAttempt = :lastSyncAttempt
        WHERE id = :id
    """)
    suspend fun updateRetryInfo(id: String, retryCount: Int, lastSyncAttempt: Long)

    @Query("DELETE FROM macro_targets WHERE id = :id")
    suspend fun delete(id: String)
}