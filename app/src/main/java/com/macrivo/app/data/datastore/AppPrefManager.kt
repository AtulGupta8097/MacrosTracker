package com.macrivo.app.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.macrivo.app.domain.model.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_pref")

@Singleton
class AppPrefManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val IS_USER_SETUP = booleanPreferencesKey("is_user_setup")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val LAST_USER_ID = stringPreferencesKey("last_user_id")
    }

    val isLoggedIn = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    val isUserSetup = context.dataStore.data.map { preferences ->
        preferences[IS_USER_SETUP] ?: false
    }

    val themeMode = context.dataStore.data.map { preferences ->
        ThemeMode.entries.find { it.name == preferences[THEME_MODE] }
            ?: ThemeMode.SYSTEM
    }

    suspend fun setLoggedIn(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = value
        }
    }

    suspend fun setUserSetup(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_USER_SETUP] = value
        }
    }

    suspend fun lastUserId(): String? {
        return context.dataStore.data.first()[LAST_USER_ID]
    }

    suspend fun setLastUserId(uid: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_USER_ID] = uid
        }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE] = mode.name
        }
    }
}