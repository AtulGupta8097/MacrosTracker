package com.example.responsiveapp.data.repository

import com.example.responsiveapp.data.datasource.local.UserProfileLocalDataSource
import com.example.responsiveapp.data.mapper.toDomain
import com.example.responsiveapp.data.mapper.toDto
import com.example.responsiveapp.data.datasource.remote.UserProfileRemoteDataSource
import com.example.responsiveapp.domain.model.UserProfile
import com.example.responsiveapp.domain.repository.UserProfileRepository

class UserProfileRepositoryImpl(
    private val local: UserProfileLocalDataSource,
    private val remote: UserProfileRemoteDataSource
) : UserProfileRepository {

    override suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        val dto = profile.toDto()

        return try {
            // Try remote first (source of truth)
            remote.save(dto)

            // Cache locally after success
            local.save(dto)

            Result.success(Unit)
        } catch (e: Exception) {
            // Remote failed → save locally for offline usage
            local.save(dto)

            Result.failure(e)
        }
    }

    override suspend fun getUserProfile(): UserProfile? {

        local.get()?.toDomain()?.let { return it }
        val remoteDto = remote.get() ?: return null
        local.save(remoteDto)

        return remoteDto.toDomain()
    }
}

