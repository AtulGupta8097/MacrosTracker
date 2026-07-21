package com.example.responsiveapp.data.repository

import android.util.Log
import com.example.responsiveapp.data.local.dao.MyMealsDao
import com.example.responsiveapp.data.mapper.toDomain
import com.example.responsiveapp.data.mapper.toEntity
import com.example.responsiveapp.data.mapper.toFirestoreDto
import com.example.responsiveapp.data.remote.dto.firebase.MyMealDto
import com.example.responsiveapp.domain.model.SyncStatus
import com.example.responsiveapp.domain.model.mymeals.MyMeal
import com.example.responsiveapp.domain.repository.MyMealRepository
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
class MyMealRepositoryImpl @Inject constructor(
    private val myMealDao: MyMealsDao,
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager,
    private val scheduler: SyncScheduler,
) : MyMealRepository {

    override suspend fun saveMeal(meal: MyMeal) {
        myMealDao.insertMyMeal(meal.toEntity())
        scheduler.schedule(SyncType.MY_MEAL)
    }

    override fun observeMeals(): Flow<List<MyMeal>> =
        myMealDao.observeMyMeals().map { meals ->
            meals.toDomain()
        }

    override suspend fun syncPending() {

        val pending = myMealDao.getPending()

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
                    "Failed to upload MyMeal: ${entity.id}",
                    e
                )

                myMealDao.updateRetryInfo(
                    id = entity.id,
                    retryCount = entity.retryCount + 1,
                    lastSyncAttempt = now
                )

                myMealDao.updateSyncStatus(
                    id = entity.id,
                    status = SyncStatus.FAILED,
                    lastSyncAttempt = now
                )

                continue
            }

            try {

                myMealDao.updateSyncStatus(
                    id = entity.id,
                    status = SyncStatus.SYNCED,
                    lastSyncAttempt = now
                )

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "Uploaded MyMeal ${entity.id} to Firestore but failed to update local sync status.",
                    e
                )
            }
        }
    }

    override suspend fun deleteMeal(id: String) {
        myMealDao.deleteMyMealsById(id)
    }


    override suspend fun fetchAndCacheAll() {

        try {

            val snapshot = collection().get().await()

            val entities = snapshot.documents.mapNotNull {
                it.toObject(MyMealDto::class.java)?.toEntity()
            }

            myMealDao.insertAllFromRemote(entities)

        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch MyMeals from Firestore", e)
        }
    }

    private fun collection() = firestore
        .collection("users")
        .document(sessionManager.requireUserId())
        .collection("my_meals")

    companion object {
        private const val TAG = "MyMealRepository"
    }
}