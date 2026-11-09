package com.macrivo.app.data.repository

import android.util.Log
import com.macrivo.app.data.local.dao.WeightLogDao
import com.macrivo.app.data.mapper.toDomain
import com.macrivo.app.data.mapper.toEntity
import com.macrivo.app.data.mapper.toFirestoreDto
import com.macrivo.app.data.remote.dto.firebase.WeightLogDto
import com.macrivo.app.domain.model.SyncStatus
import com.macrivo.app.domain.model.weight.WeightLog
import com.macrivo.app.domain.repository.WeightRepository
import com.macrivo.app.domain.session.SessionManager
import com.macrivo.app.sync.SyncScheduler
import com.macrivo.app.sync.SyncType
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeightRepositoryImpl @Inject constructor(
    private val dao: WeightLogDao,
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager,
    private val scheduler: SyncScheduler,
) : WeightRepository {

    override fun observeLatestWeight(): Flow<WeightLog?> =
        dao.observeLatestWeight()
            .map { it?.toDomain() }

    override fun observeWeightHistory(): Flow<List<WeightLog>> =
        dao.observeWeightHistory()
            .map { logs ->
                logs.map { it.toDomain() }
            }

    override fun observeWeightBetweenDates(
        startDate: Long,
        endDate: Long,
    ): Flow<List<WeightLog>> =
        dao.observeWeightBetweenDates(
            startDate = startDate,
            endDate = endDate,
        ).map { logs ->
            logs.map { it.toDomain() }
        }

    override suspend fun saveWeight(
        weightLog: WeightLog,
    ) {
        dao.insertWeight(
            weightLog.toEntity()
        )

        scheduler.schedule(
            SyncType.WEIGHT_LOG,
        )
    }

    override suspend fun updateWeight(
        weightLog: WeightLog,
    ) {
        dao.updateWeight(
            weightLog.toEntity()
        )

        scheduler.schedule(
            SyncType.WEIGHT_LOG,
        )
    }

    override suspend fun syncPending() {

        val pending = dao.getPending()

        for (entity in pending) {

            val now = System.currentTimeMillis()

            try {

                collection()
                    .document(entity.id)
                    .set(entity.toFirestoreDto())
                    .await()

                dao.updateSyncStatus(
                    id = entity.id,
                    status = SyncStatus.SYNCED,
                    lastSyncAttempt = now,
                )

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "Failed to upload WeightLog: ${entity.id}",
                    e,
                )

                dao.updateRetryInfo(
                    id = entity.id,
                    retryCount = entity.retryCount + 1,
                    lastSyncAttempt = now,
                )

                dao.updateSyncStatus(
                    id = entity.id,
                    status = SyncStatus.FAILED,
                    lastSyncAttempt = now,
                )
            }
        }
    }

    override suspend fun fetchAndCacheAll() {

        try {

            val snapshot = collection()
                .get()
                .await()

            val entities = snapshot.documents
                .mapNotNull { document ->
                    document
                        .toObject(WeightLogDto::class.java)
                        ?.toEntity()
                }

            dao.insertAllFromRemote(entities)

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Failed to fetch WeightLogs from Firestore",
                e,
            )
        }
    }

    private fun collection() =
        firestore
            .collection("users")
            .document(sessionManager.requireUserId())
            .collection("weight_logs")

    companion object {
        private const val TAG = "WeightRepository"
    }
}
