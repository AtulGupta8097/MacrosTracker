package com.example.responsiveapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.core.DataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

private const val USER_PREFS_NAME = "user_preferences"

val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFS_NAME
)

class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext context: Context
) {
    val dataStore: DataStore<Preferences> = context.userPreferencesDataStore
}
