package com.macrivo.app.data.repository

import android.util.Log
import com.macrivo.app.data.local.dao.CustomFoodDao
import com.macrivo.app.data.mapper.toDomain
import com.macrivo.app.data.mapper.toEntity
import com.macrivo.app.data.mapper.toFirestoreDto
import com.macrivo.app.data.remote.dto.firebase.CustomFoodDto
import com.macrivo.app.domain.model.SyncStatus
import com.macrivo.app.domain.model.myfood.CustomFood
import com.macrivo.app.domain.repository.CustomFoodRepository
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
class CustomFoodRepositoryImpl @Inject constructor(
    private val dao: CustomFoodDao,
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager,
    private val scheduler: SyncScheduler,
) : CustomFoodRepository {

    override fun observeAll(): Flow<List<CustomFood>> =
        dao.observeAll().map { foods ->
            foods.map { it.toDomain() }
        }

    override suspend fun save(food: CustomFood) {
        dao.insert(food.toEntity())
        scheduler.schedule(SyncType.CUSTOM_FOOD)
    }

    override suspend fun delete(foodId: String) {
        dao.deleteById(foodId)
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

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "Failed to upload CustomFood: ${entity.id}",
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
                    "Uploaded CustomFood ${entity.id} to Firestore but failed to update local sync status.",
                    e
                )
            }
        }
    }

    override suspend fun fetchAndCacheAll() {
        try {

            val snapshot = collection().get().await()

            val entities = snapshot.documents.mapNotNull {
                it.toObject(CustomFoodDto::class.java)?.toEntity()
            }

            dao.insertAllFromRemote(entities)

        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch CustomFoods from Firestore", e)
        }
    }

    private fun collection() = firestore
        .collection("users")
        .document(sessionManager.requireUserId())
        .collection("custom_foods")

    companion object {
        private const val TAG = "CustomFoodRepository"
    }
}