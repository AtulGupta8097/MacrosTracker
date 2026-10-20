package com.example.responsiveapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.responsiveapp.data.local.entity.SavedFoodEntity
import com.example.responsiveapp.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedFoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: SavedFoodEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFromRemote(foods: List<SavedFoodEntity>)

    @Query("""
        SELECT *
        FROM saved_foods
        ORDER BY savedAt DESC
    """)
    fun observeAll(): Flow<List<SavedFoodEntity>>

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM saved_foods WHERE id = :foodId
        )
    """)
    fun observeIsSaved(foodId: String): Flow<Boolean>

    @Query("""
        DELETE FROM saved_foods
        WHERE id = :id
    """)
    suspend fun deleteById(id: String)

    @Query("""
        SELECT *
        FROM saved_foods
        WHERE syncStatus IN ('PENDING', 'FAILED')
        ORDER BY savedAt ASC
    """)
    suspend fun getPending(): List<SavedFoodEntity>

    @Query("""
        UPDATE saved_foods
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
        UPDATE saved_foods
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
