package com.example.responsiveapp.data.repository

import com.example.responsiveapp.data.local.dao.FoodLogDao
import com.example.responsiveapp.data.mapper.toDomain
import com.example.responsiveapp.data.mapper.toEntity
import com.example.responsiveapp.data.mapper.toFirebaseDto
import com.example.responsiveapp.domain.model.FoodLog
import com.example.responsiveapp.domain.repository.FoodLogRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class FoodLogRepositoryImpl (
    private val foodLogDao: FoodLogDao,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : FoodLogRepository {
    
    private fun getFoodLogsCollection() = firestore
        .collection("users")
        .document(getCurrentUserId())
        .collection("food_logs")
    
    private fun getCurrentUserId(): String {
        return firebaseAuth.currentUser?.uid 
            ?: throw IllegalStateException("User not authenticated")
    }
    
    override suspend fun logFood(foodLog: FoodLog): Result<FoodLog> {
        return try {

            val entity = foodLog.toEntity()
            foodLogDao.insertFoodLog(entity)

            val savedLog = foodLog.copy(isSynced = false)

            try {
                syncLogToFirestore(savedLog)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            
            Result.success(savedLog)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun syncPendingLogs(userId: String): Result<Int> {
        return try {
            val pendingLogs = foodLogDao.getPendingLogs(userId)
            var syncedCount = 0
            
            for (entity in pendingLogs) {
                try {
                    val foodLog = entity.toDomain()
                    syncLogToFirestore(foodLog)

                    foodLogDao.updateSyncStatus(entity.id, "SYNCED")
                    syncedCount++
                    
                } catch (e: Exception) {
                    e.printStackTrace()
                    foodLogDao.updateSyncStatus(entity.id, "FAILED")
                }
            }
            
            Result.success(syncedCount)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun syncLogToFirestore(foodLog: FoodLog) {
        val firebaseDto = foodLog.toFirebaseDto()
        
        getFoodLogsCollection()
            .document(foodLog.id)
            .set(firebaseDto)
            .await()

        foodLogDao.updateSyncStatus(foodLog.id, "SYNCED")
    }
}