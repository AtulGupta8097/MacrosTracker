package com.example.responsiveapp.data.datasource

import androidx.datastore.preferences.core.edit
import com.example.responsiveapp.data.remote.dto.UserProfileDto
import com.example.responsiveapp.data.datastore.UserPreferencesKeys
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first

class UserProfileLocalDataSource @Inject constructor(
    private val preferences: UserPreferencesDataStore
) {

    suspend fun save(dto: UserProfileDto) {
        preferences.dataStore.edit { prefs ->
            dto.gender?.let { prefs[UserPreferencesKeys.GENDER] = it }
            dto.age?.let { prefs[UserPreferencesKeys.AGE] = it }
            dto.weight?.let { prefs[UserPreferencesKeys.WEIGHT] = it }
            dto.height?.let { prefs[UserPreferencesKeys.HEIGHT] = it }
            dto.activityLevel?.let { prefs[UserPreferencesKeys.ACTIVITY] = it }
            dto.goal?.let { prefs[UserPreferencesKeys.GOAL] = it }
        }
    }

    suspend fun get(): UserProfileDto? {
        val prefs = preferences.dataStore.data.first()

        val gender = prefs[UserPreferencesKeys.GENDER] ?: return null

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