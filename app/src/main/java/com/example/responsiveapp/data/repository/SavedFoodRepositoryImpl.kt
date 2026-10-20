package com.example.responsiveapp.data.repository

import android.util.Log
import com.example.responsiveapp.data.local.dao.SavedFoodDao
import com.example.responsiveapp.data.mapper.toDomain
import com.example.responsiveapp.data.mapper.toEntity
import com.example.responsiveapp.data.mapper.toFirestoreDto
import com.example.responsiveapp.data.mapper.toSavedEntity
import com.example.responsiveapp.data.remote.dto.firebase.SavedFoodDto
import com.example.responsiveapp.domain.model.FoodDetail
import com.example.responsiveapp.domain.model.SyncStatus
import com.example.responsiveapp.domain.repository.SavedFoodRepository
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
class SavedFoodRepositoryImpl @Inject constructor(
    private val dao: SavedFoodDao,
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager,
    private val scheduler: SyncScheduler,
) : SavedFoodRepository {

    override fun observeAll(): Flow<List<FoodDetail>> =
        dao.observeAll().map { foods -> foods.map { it.toDomain() } }

    override fun observeIsSaved(foodId: String): Flow<Boolean> =
        dao.observeIsSaved(foodId)

    override suspend fun save(food: FoodDetail) {
        dao.insert(food.toSavedEntity())
        scheduler.schedule(SyncType.SAVED_FOOD)
    }

    override suspend fun remove(foodId: String) {
        dao.deleteById(foodId)


        try {
            collection().document(foodId).delete().await()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete SavedFood $foodId from Firestore", e)
        }
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

                Log.e(TAG, "Failed to upload SavedFood: ${entity.id}", e)

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
                    "Uploaded SavedFood ${entity.id} to Firestore but failed to update local sync status.",
                    e
                )
            }
        }
    }

    override suspend fun fetchAndCacheAll() {
        try {

            val snapshot = collection().get().await()

            val entities = snapshot.documents.mapNotNull {
                it.toObject(SavedFoodDto::class.java)?.toEntity()
            }

            dao.insertAllFromRemote(entities)

        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch SavedFoods from Firestore", e)
        }
    }

    private fun collection() = firestore
        .collection("users")
        .document(sessionManager.requireUserId())
        .collection("saved_foods")

    companion object {
        private const val TAG = "SavedFoodRepository"
    }
}
