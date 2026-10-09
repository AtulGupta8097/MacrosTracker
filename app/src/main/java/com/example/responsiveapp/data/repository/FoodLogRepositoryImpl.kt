package com.example.responsiveapp.data.repository

import android.util.Log
import com.example.responsiveapp.core.utils.DateUtils.toLocalDateKey
import com.example.responsiveapp.data.local.dao.FoodLogDao
import com.example.responsiveapp.data.mapper.toDomain
import com.example.responsiveapp.data.mapper.toEntity
import com.example.responsiveapp.data.mapper.toFirestoreDto
import com.example.responsiveapp.data.remote.dto.firebase.FoodLogDto
import com.example.responsiveapp.domain.model.foodlog.FoodLog
import com.example.responsiveapp.domain.model.SyncStatus
import com.example.responsiveapp.domain.repository.FoodLogRepository
import com.example.responsiveapp.domain.session.SessionManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions // CHANGED: needed to merge-set the date doc below
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun observeFoodLogsForDate(date: Long): Flow<List<FoodLog>> =
        foodLogDao
            .getFoodLogsForDate(date)
            .map { entities ->
                entities.map { entity ->
                    entity.toDomain()
                }
            }

    override fun observeLoggedDates(): Flow<List<Long>> =
        foodLogDao.observeLoggedDates()

    override fun observeMealCountsBetweenDates(
        startDate: Long,
        endDate: Long,
    ): Flow<Map<Long, Int>> =
        foodLogDao
            .observeMealCountsBetweenDates(startDate, endDate)
            .map { rows -> rows.associate { it.date to it.count } }

    override suspend fun syncPending() {

        val pending = foodLogDao.getPending()

        for (entity in pending) {

            val now = System.currentTimeMillis()

            try {

                val dateDocRef = collection()
                    .document(entity.date.toLocalDateKey())

                dateDocRef
                    .set(mapOf("date" to entity.date), SetOptions.merge())
                    .await()

                dateDocRef
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

        val userId = sessionManager.requireUserId()

        Log.d(TAG, "Fetching food logs for user: $userId")

        val dateDocs = collection()
            .get()
            .await()

        Log.d(
            TAG,
            "Date documents found: ${dateDocs.documents.size}"
        )

        for (dateDoc in dateDocs.documents) {

            Log.d(
                TAG,
                "Date document: ${dateDoc.id}"
            )

            val logsSnapshot = dateDoc.reference
                .collection("logs")
                .get()
                .await()

            Log.d(
                TAG,
                "Logs found for ${dateDoc.id}: ${logsSnapshot.documents.size}"
            )

            for (logDoc in logsSnapshot.documents) {
                Log.d(
                    TAG,
                    "Raw Firestore log ${logDoc.id}: ${logDoc.data}"
                )
            }
        }

        val entities = dateDocs.documents.flatMap { dateDoc ->

            dateDoc.reference
                .collection("logs")
                .get()
                .await()
                .documents
                .mapNotNull { document ->

                    try {
                        document
                            .toObject(FoodLogDto::class.java)
                            ?.toEntity()

                    } catch (e: Exception) {

                        Log.e(
                            TAG,
                            "Failed to convert document ${document.id}",
                            e
                        )

                        null
                    }
                }
        }

        Log.d(
            TAG,
            "Total entities converted: ${entities.size}"
        )

        foodLogDao.insertAllFromRemote(entities)

        Log.d(
            TAG,
            "Inserted entities into Room: ${entities.size}"
        )
    }

    private fun collection() = firestore
        .collection("users")
        .document(sessionManager.requireUserId())
        .collection("food_logs")

    companion object {
        private const val TAG = "FoodLogRepository"
    }
}