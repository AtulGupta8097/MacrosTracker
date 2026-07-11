package com.example.responsiveapp.data.repository

import com.example.responsiveapp.data.local.dao.MacroTargetDao
import com.example.responsiveapp.data.mapper.toDomain
import com.example.responsiveapp.data.mapper.toEntity
import com.example.responsiveapp.domain.model.SyncStatus
import com.example.responsiveapp.domain.model.macros.MacroTarget
import com.example.responsiveapp.domain.repository.MacroTargetRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MacroTargetRepositoryImpl @Inject constructor(
    private val dao: MacroTargetDao
) : MacroTargetRepository {

    override suspend fun saveTarget(target: MacroTarget): Result<Unit> {
        return try {
            dao.insert(target.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentTarget(): MacroTarget? {
        return dao.getCurrentTarget()?.toDomain()
    }

    override suspend fun getPendingTargets(): List<MacroTarget> {
        return dao.getPending().map { it.toDomain() }
    }

    override suspend fun updateSyncStatus(targetId: String, status: SyncStatus) {
        dao.updateSyncStatus(
            id              = targetId,
            status          = status,
            lastSyncAttempt = System.currentTimeMillis()
        )
    }

    override suspend fun updateRetryInfo(targetId: String, retryCount: Int, lastSyncAttempt: Long) {
        dao.updateRetryInfo(
            id              = targetId,
            retryCount      = retryCount,
            lastSyncAttempt = lastSyncAttempt
        )
    }
}