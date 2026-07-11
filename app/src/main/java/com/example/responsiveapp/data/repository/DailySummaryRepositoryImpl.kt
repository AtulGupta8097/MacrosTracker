package com.example.responsiveapp.data.repository

import com.example.responsiveapp.data.local.dao.DailySummaryDao
import com.example.responsiveapp.data.mapper.toDomain
import com.example.responsiveapp.data.mapper.toEntity
import com.example.responsiveapp.domain.model.DailySummary
import com.example.responsiveapp.domain.model.SyncStatus
import com.example.responsiveapp.domain.repository.DailySummaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailySummaryRepositoryImpl @Inject constructor(
    private val dao: DailySummaryDao
) : DailySummaryRepository {

    override suspend fun insert(summary: DailySummary) {
        dao.insert(summary.toEntity())
    }

    override suspend fun update(summary: DailySummary) {
        dao.update(summary.toEntity())
    }

    override fun observeForDate(startOfDay: Long): Flow<DailySummary?> {
        return dao.observeForDate(startOfDay).map { it?.toDomain() }
    }

    override suspend fun getForDate(startOfDay: Long): DailySummary? {
        return dao.getForDate(startOfDay)?.toDomain()
    }

    override suspend fun syncPending() {
        TODO("Not yet implemented")
    }

}