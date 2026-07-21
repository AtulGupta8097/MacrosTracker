package com.example.responsiveapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.responsiveapp.data.local.entity.MyMealsEntity
import com.example.responsiveapp.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MyMealsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMyMeal(meal: MyMealsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFromRemote(meals: List<MyMealsEntity>)

    @Query("""
        SELECT *
        FROM my_meals
        ORDER BY createdAt DESC
    """)
    fun observeMyMeals(): Flow<List<MyMealsEntity>>

    @Query("""
        DELETE FROM my_meals
        WHERE id = :id
    """)
    suspend fun deleteMyMealsById(id: String)

    @Query("""
        SELECT *
        FROM my_meals
        WHERE syncStatus IN ('PENDING', 'FAILED')
        ORDER BY createdAt ASC
    """)
    suspend fun getPending(): List<MyMealsEntity>

    @Query("""
        UPDATE my_meals
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
        UPDATE my_meals
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