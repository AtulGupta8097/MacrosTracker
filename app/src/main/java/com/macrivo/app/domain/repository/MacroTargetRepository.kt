package com.macrivo.app.domain.repository

import com.macrivo.app.domain.model.macros.MacroTarget

interface MacroTargetRepository {

    suspend fun saveTarget(target: MacroTarget)

    suspend fun getCurrentTarget(): MacroTarget?

    suspend fun syncPending()

    suspend fun fetchAndCacheAll()
}