package com.example.responsiveapp.data.repository

import android.util.Log
import com.example.responsiveapp.data.local.dao.CustomFoodDao
import com.example.responsiveapp.data.mapper.toDomain
import com.example.responsiveapp.data.mapper.toEntity
import com.example.responsiveapp.data.mapper.toFirestoreDto
import com.example.responsiveapp.domain.model.SyncStatus
import com.example.responsiveapp.domain.model.myfood.CustomFood
import com.example.responsiveapp.domain.repository.CustomFoodRepository
import com.example.responsiveapp.domain.session.SessionManager
import com.example.responsiveapp.sync.SyncScheduler
import com.example.responsiveapp.sync.SyncType
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

            dao.updateSyncStatus(
                id = entity.id,
                status = SyncStatus.SYNCING,
                lastSyncAttempt = now
            )

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

    private fun collection() = firestore
        .collection("users")
        .document(sessionManager.requireUserId())
        .collection("custom_foods")

    companion object {
        private const val TAG = "CustomFoodRepository"
    }
}