package com.example.responsiveapp.domain.repository

import com.example.responsiveapp.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {

    fun observeUserProfile(): Flow<UserProfile?>

    suspend fun saveUserProfile(profile: UserProfile)

    suspend fun getUserProfile(): UserProfile?

    suspend fun syncPending()
}
