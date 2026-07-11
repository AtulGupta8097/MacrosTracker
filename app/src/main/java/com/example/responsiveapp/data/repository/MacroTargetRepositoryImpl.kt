package com.example.responsiveapp.data.repository

import com.example.responsiveapp.data.local.dao.MacroTargetDao
import com.example.responsiveapp.data.local.entity.MacroTargetEntity
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

    override suspend fun saveTarget(target: MacroTarget) {
        dao.insert(target.toEntity())
    }

    override suspend fun getCurrentTarget(): MacroTarget? {
        return dao.getCurrentTarget()?.toDomain()
    }

    override suspend fun syncPending() {
        TODO("Not yet implemented")
    }

}