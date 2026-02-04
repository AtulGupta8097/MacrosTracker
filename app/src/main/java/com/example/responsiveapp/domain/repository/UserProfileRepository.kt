package com.example.responsiveapp.domain.repository

import com.example.responsiveapp.domain.model.UserProfile

interface UserProfileRepository {

    suspend fun saveUserProfile(profile: UserProfile) :  Result<Unit>

    suspend fun getUserProfile(): UserProfile?
}