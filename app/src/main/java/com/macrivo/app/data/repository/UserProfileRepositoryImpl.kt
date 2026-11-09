package com.macrivo.app.data.repository

import android.util.Log
import com.macrivo.app.data.local.dao.UserProfileDao
import com.macrivo.app.data.mapper.toDomain
import com.macrivo.app.data.mapper.toEntity
import com.macrivo.app.data.mapper.toFirestoreDto
import com.macrivo.app.data.remote.dto.firebase.UserProfileDto
import com.macrivo.app.domain.model.SyncStatus
import com.macrivo.app.domain.model.UserProfile
import com.macrivo.app.domain.repository.UserProfileRepository
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
class UserProfileRepositoryImpl @Inject constructor(
    private val dao: UserProfileDao,
    private val firestore: FirebaseFirestore,
    private val sessionManager: SessionManager,
    private val scheduler: SyncScheduler,
) : UserProfileRepository {

    override fun observeUserProfile(): Flow<UserProfile?> =
        dao.observeProfile().map { it?.toDomain() }

    override suspend fun saveUserProfile(profile: UserProfile) {

        val uid = sessionManager.requireUserId()

        val stamped = profile.copy(
            id = uid,
            updatedAt = System.currentTimeMillis()
        )

        dao.insert(stamped.toEntity())

        scheduler.schedule(SyncType.USER_PROFILE)
    }

    override suspend fun getUserName(): String? {
       return sessionManager.currentUserName()
    }

    override suspend fun getUserProfile(): UserProfile? {

        dao.getProfile()?.let {
            return it.toDomain()
        }

        val uid = sessionManager.currentUserId()
            ?: return null

        val remoteDto = firestore
            .collection("users")
            .document(uid)
            .get()
            .await()
            .toObject(UserProfileDto::class.java)
            ?: return null

        val domain = remoteDto.toDomain(id = uid)
            ?: return null

        dao.insert(domain.toEntity(syncStatus = SyncStatus.SYNCED))

        return domain
    }

    override suspend fun syncPending() {

        val pending = dao.getPending()

        for (entity in pending) {

            val now = System.currentTimeMillis()

            try {

                firestore
                    .collection("users")
                    .document(entity.id)
                    .set(entity.toFirestoreDto())
                    .await()

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "Failed to upload UserProfile: ${entity.id}",
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
                    "Uploaded UserProfile ${entity.id} to Firestore but failed to update local sync status.",
                    e
                )
            }
        }
    }

    companion object {
        private const val TAG = "UserProfileRepository"
    }
}
