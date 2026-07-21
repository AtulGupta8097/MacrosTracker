package com.example.responsiveapp.data.repository

import android.util.Log
import com.example.responsiveapp.core.utils.toLocalDateKey
import com.example.responsiveapp.data.local.dao.FoodLogDao
import com.example.responsiveapp.data.mapper.toEntity
import com.example.responsiveapp.data.mapper.toFirestoreDto
import com.example.responsiveapp.data.remote.dto.firebase.FoodLogDto
import com.example.responsiveapp.domain.model.foodlog.FoodLog
import com.example.responsiveapp.domain.model.SyncStatus
import com.example.responsiveapp.domain.repository.FoodLogRepository
import com.example.responsiveapp.domain.session.SessionManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodLogRepositoryImpl @Inject constructor(
    private val foodLogDao: FoodLogDao,
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager,
) : FoodLogRepository {

    override suspend fun logFood(foodLog: FoodLog) {
        foodLogDao.insertFoodLog(foodLog.toEntity())
    }

    override suspend fun syncPending() {

        val pending = foodLogDao.getPending()

        for (entity in pending) {

            val now = System.currentTimeMillis()

            try {

                collection()
                    .document(entity.date.toLocalDateKey())
                    .collection("logs")
                    .document(entity.id)
                    .set(entity.toFirestoreDto())
                    .await()

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "Failed to upload FoodLog: ${entity.id}",
                    e
                )

                foodLogDao.updateRetryInfo(
                    logId = entity.id,
                    retryCount = entity.retryCount + 1,
                    lastSyncAttempt = now
                )

                foodLogDao.updateSyncStatus(
                    logId = entity.id,
                    status = SyncStatus.FAILED,
                    lastSyncAttempt = now
                )

                continue
            }

            try {

                foodLogDao.updateSyncStatus(
                    logId = entity.id,
                    status = SyncStatus.SYNCED,
                    lastSyncAttempt = now
                )

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "Uploaded FoodLog ${entity.id} to Firestore but failed to update local sync status.",
                    e
                )
            }
        }
    }


    override suspend fun fetchAndCacheAll() {

        try {

            // food_logs/{dateKey} is a date-bucket document; the actual
            // log entries live in its "logs" subcollection.
            val dateDocs = collection().get().await()

            val entities = dateDocs.documents.flatMap { dateDoc ->
                dateDoc.reference
                    .collection("logs")
                    .get()
                    .await()
                    .documents
                    .mapNotNull { it.toObject(FoodLogDto::class.java)?.toEntity() }
            }

            foodLogDao.insertAllFromRemote(entities)

        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch FoodLogs from Firestore", e)
        }
    }

    private fun collection() = firestore
        .collection("users")
        .document(sessionManager.requireUserId())
        .collection("food_logs")

    companion object {
        private const val TAG = "FoodLogRepository"
    }
}