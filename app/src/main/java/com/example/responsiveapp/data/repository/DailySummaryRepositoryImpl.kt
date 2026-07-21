package com.example.responsiveapp.data.repository

import android.util.Log
import com.example.responsiveapp.data.local.dao.DailySummaryDao
import com.example.responsiveapp.data.mapper.toDomain
import com.example.responsiveapp.data.mapper.toEntity
import com.example.responsiveapp.data.mapper.toFirestoreDto
import com.example.responsiveapp.data.remote.dto.firebase.DailySummaryDto
import com.example.responsiveapp.domain.model.DailySummary
import com.example.responsiveapp.domain.model.SyncStatus
import com.example.responsiveapp.domain.repository.DailySummaryRepository
import com.example.responsiveapp.domain.session.SessionManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailySummaryRepositoryImpl @Inject constructor(
    private val dao: DailySummaryDao,
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager,
) : DailySummaryRepository {

    override suspend fun insert(summary: DailySummary) {
        dao.insert(summary.toEntity())
    }

    override suspend fun update(summary: DailySummary) {
        dao.update(summary.toEntity())
    }

    override fun observeForDate(date: Long): Flow<DailySummary?> =
        dao.observeForDate(date).map { it?.toDomain() }

    override suspend fun getForDate(date: Long): DailySummary? =
        dao.getForDate(date)?.toDomain()

    override suspend fun syncPending() {

        val pending = dao.getPending()

        for (entity in pending) {

            val now = System.currentTimeMillis()

            try {

                collection()
                    .document(entity.date.toString())
                    .set(entity.toFirestoreDto())
                    .await()

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "Failed to upload DailySummary: ${entity.date}",
                    e
                )

                dao.updateRetryInfo(
                    date = entity.date,
                    retryCount = entity.retryCount + 1,
                    lastSyncAttempt = now
                )

                dao.updateSyncStatus(
                    date = entity.date,
                    status = SyncStatus.FAILED,
                    lastSyncAttempt = now
                )

                continue
            }

            try {

                dao.updateSyncStatus(
                    date = entity.date,
                    status = SyncStatus.SYNCED,
                    lastSyncAttempt = now
                )

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "Uploaded DailySummary ${entity.date} to Firestore but failed to update local sync status.",
                    e
                )
            }
        }
    }

    override suspend fun fetchAndCacheAll() {

        try {

            val snapshot = collection().get().await()

            val entities = snapshot.documents.mapNotNull {
                it.toObject(DailySummaryDto::class.java)?.toEntity()
            }

            dao.insertAllFromRemote(entities)

        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch DailySummaries from Firestore", e)
        }
    }

    private fun collection() = firestore
        .collection("users")
        .document(sessionManager.requireUserId())
        .collection("daily_summaries")

    companion object {
        private const val TAG = "DailySummaryRepository"
    }
}