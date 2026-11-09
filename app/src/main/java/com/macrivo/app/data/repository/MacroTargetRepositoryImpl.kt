package com.macrivo.app.data.repository

import android.util.Log
import com.macrivo.app.data.local.dao.MacroTargetDao
import com.macrivo.app.data.mapper.toDomain
import com.macrivo.app.data.mapper.toEntity
import com.macrivo.app.data.mapper.toFirestoreDto
import com.macrivo.app.data.remote.dto.firebase.MacroTargetDto
import com.macrivo.app.domain.model.SyncStatus
import com.macrivo.app.domain.model.macros.MacroTarget
import com.macrivo.app.domain.repository.MacroTargetRepository
import com.macrivo.app.domain.session.SessionManager
import com.macrivo.app.sync.SyncScheduler
import com.macrivo.app.sync.SyncType
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MacroTargetRepositoryImpl @Inject constructor(
    private val dao: MacroTargetDao,
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager,
    private val scheduler: SyncScheduler,
) : MacroTargetRepository {

    override suspend fun saveTarget(target: MacroTarget) {
        dao.insert(target.toEntity())
        scheduler.schedule(SyncType.MACRO_TARGET)
    }

    override suspend fun getCurrentTarget(): MacroTarget? =
        dao.getCurrentTarget()?.toDomain()

    override suspend fun syncPending() {

        val pending = dao.getPending()

        for (entity in pending) {

            val now = System.currentTimeMillis()

            try {

                collection()
                    .document(entity.id)
                    .set(entity.toFirestoreDto())
                    .await()

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "Failed to upload MacroTarget: ${entity.id}",
                    e
                )

                dao.updateRetryInfo(
                    id = entity.id,
                    retryCount = entity.retryCount + 1,
                    lastSyncAttempt = now
                )

                dao.updateSyncStatus(
                    id = entity.id,
                    status = SyncStatus.FAILED,
                    lastSyncAttempt = now
                )

                continue
            }

            try {

                dao.updateSyncStatus(
                    id = entity.id,
                    status = SyncStatus.SYNCED,
                    lastSyncAttempt = now
                )

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "Uploaded MacroTarget ${entity.id} to Firestore but failed to update local sync status.",
                    e
                )
            }
        }
    }

    override suspend fun fetchAndCacheAll() {

        try {

            val snapshot = collection().get().await()

            val entities = snapshot.documents.mapNotNull {
                it.toObject(MacroTargetDto::class.java)?.toEntity()
            }

            dao.insertAllFromRemote(entities)

        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch MacroTargets from Firestore", e)
        }
    }

    private fun collection() = firestore
        .collection("users")
        .document(sessionManager.requireUserId())
        .collection("macro_targets")

    companion object {
        private const val TAG = "MacroTargetRepository"
    }
}