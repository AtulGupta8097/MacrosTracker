package com.example.responsiveapp.domain.repository

import com.example.responsiveapp.domain.model.SyncStatus
import com.example.responsiveapp.domain.model.macros.MacroTarget

interface MacroTargetRepository {

    suspend fun saveTarget(target: MacroTarget): Result<Unit>

    suspend fun getCurrentTarget(): MacroTarget?

    suspend fun getPendingTargets(): List<MacroTarget>

    suspend fun updateSyncStatus(targetId: String, status: SyncStatus)

    suspend fun updateRetryInfo(targetId: String, retryCount: Int, lastSyncAttempt: Long)
}