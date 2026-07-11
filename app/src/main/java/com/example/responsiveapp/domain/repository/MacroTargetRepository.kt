package com.example.responsiveapp.domain.repository

import com.example.responsiveapp.domain.model.SyncStatus
import com.example.responsiveapp.domain.model.macros.MacroTarget

interface MacroTargetRepository {

    suspend fun saveTarget(target: MacroTarget)

    suspend fun getCurrentTarget(): MacroTarget?

    suspend fun syncPending()
}