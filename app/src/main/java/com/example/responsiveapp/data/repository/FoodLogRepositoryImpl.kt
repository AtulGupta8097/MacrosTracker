package com.example.responsiveapp.data.repository

import com.example.responsiveapp.data.local.dao.FoodLogDao
import com.example.responsiveapp.data.mapper.toDomain
import com.example.responsiveapp.data.mapper.toEntity
import com.example.responsiveapp.domain.model.FoodLog
import com.example.responsiveapp.domain.model.SyncStatus
import com.example.responsiveapp.domain.repository.FoodLogRepository
import com.example.responsiveapp.domain.session.SessionManager
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodLogRepositoryImpl @Inject constructor(
    private val foodLogDao: FoodLogDao,
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager
) : FoodLogRepository {

    override suspend fun logFood(foodLog: FoodLog): Result<FoodLog> {
        return try {
            foodLogDao.insertFoodLog(foodLog.toEntity())
            Result.success(foodLog)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncPendingLogs(userId: String): Result<Int> {
        TODO("Not yet implemented")
    }


    private fun getFoodLogsCollection() = firestore
        .collection("users")
        .document(sessionManager.requireUserId())
        .collection("food_logs")

    private suspend fun syncLogToFirestore(foodLog: FoodLog) {

    }
}