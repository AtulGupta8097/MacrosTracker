package com.example.responsiveapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.responsiveapp.data.local.entity.CustomFoodEntity
import com.example.responsiveapp.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomFoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: CustomFoodEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFromRemote(foods: List<CustomFoodEntity>)

    @Query("""
        SELECT *
        FROM custom_foods
        ORDER BY createdAt DESC
    """)
    fun observeAll(): Flow<List<CustomFoodEntity>>

    @Query("""
        DELETE FROM custom_foods
        WHERE id = :id
    """)
    suspend fun deleteById(id: String)

    @Query("""
        SELECT *
        FROM custom_foods
        WHERE syncStatus IN ('PENDING', 'FAILED')
        ORDER BY createdAt ASC
    """)
    suspend fun getPending(): List<CustomFoodEntity>

    @Query("""
        UPDATE custom_foods
        SET
            syncStatus = :status,
            lastSyncAttempt = :lastSyncAttempt
        WHERE id = :id
    """)
    suspend fun updateSyncStatus(
        id: String,
        status: SyncStatus,
        lastSyncAttempt: Long
    )

    @Query("""
        UPDATE custom_foods
        SET
            retryCount = :retryCount,
            lastSyncAttempt = :lastSyncAttempt
        WHERE id = :id
    """)
    suspend fun updateRetryInfo(
        id: String,
        retryCount: Int,
        lastSyncAttempt: Long
    )
}