package com.macrivo.app.domain.repository

import com.macrivo.app.domain.model.macros.MacroTarget
import kotlinx.coroutines.flow.Flow

interface MacroTargetRepository {

    suspend fun saveTarget(target: MacroTarget)

    fun observeCurrentTarget(): Flow<MacroTarget?>

    suspend fun getCurrentTarget(): MacroTarget?

    suspend fun syncPending()

    suspend fun fetchAndCacheAll()
}