package com.example.responsiveapp.data.repository

import androidx.datastore.preferences.core.edit
import com.example.responsiveapp.data.datastore.UserPreferencesDataStore
import com.example.responsiveapp.data.datastore.UserPreferencesKeys
import com.example.responsiveapp.data.mapper.toDomain
import com.example.responsiveapp.data.mapper.toDto
import com.example.responsiveapp.data.remote.dto.UserProfileDto
import com.example.responsiveapp.domain.model.UserProfile
import com.example.responsiveapp.domain.repository.UserProfileRepository
import com.example.responsiveapp.domain.session.SessionManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val preferences: UserPreferencesDataStore,
    private val sessionManager: SessionManager
) : UserProfileRepository {

    override suspend fun saveUserProfile(profile: UserProfile) {

        val dto = profile.toDto()

        try {

            val uid = sessionManager.requireUserId()

            firestore
                .collection("users")
                .document(uid)
                .set(dto)
                .await()

        } finally {
            saveToLocal(dto)
        }
    }

    override suspend fun getUserProfile(): UserProfile? {

        getLocalProfile()?.let {
            return it.toDomain()
        }

        val uid = sessionManager.currentUserId()
            ?: return null

        val remoteDto = firestore
            .collection("users")
            .document(uid)
            .get()
            .await()
            .toObject(UserProfileDto::class.java)
            ?: return null

        saveToLocal(remoteDto)

        return remoteDto.toDomain()
    }

    private suspend fun saveToLocal(dto: UserProfileDto) {
        preferences.dataStore.edit { prefs ->

            dto.gender?.let {
                prefs[UserPreferencesKeys.GENDER] = it
            }
            dto.age?.let {
                prefs[UserPreferencesKeys.AGE] = it
            }
            dto.weight?.let {
                prefs[UserPreferencesKeys.WEIGHT] = it
            }
            dto.height?.let {
                prefs[UserPreferencesKeys.HEIGHT] = it
            }
            dto.activityLevel?.let {
                prefs[UserPreferencesKeys.ACTIVITY] = it
            }
            dto.goal?.let {
                prefs[UserPreferencesKeys.GOAL] = it
            }
        }
    }

    private suspend fun getLocalProfile(): UserProfileDto? {

        val prefs = preferences.dataStore.data.first()

        val gender = prefs[UserPreferencesKeys.GENDER]
            ?: return null

        return UserProfileDto(
            gender = gender,
            age = prefs[UserPreferencesKeys.AGE],
            weight = prefs[UserPreferencesKeys.WEIGHT],
            height = prefs[UserPreferencesKeys.HEIGHT],
            activityLevel = prefs[UserPreferencesKeys.ACTIVITY],
            goal = prefs[UserPreferencesKeys.GOAL]
        )
    }
}