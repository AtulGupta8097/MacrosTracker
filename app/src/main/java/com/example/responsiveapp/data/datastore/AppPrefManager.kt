package com.example.responsiveapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.responsiveapp.domain.model.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore("app_pref")

@Singleton
class AppPrefManager @Inject constructor(@param:ApplicationContext private val context: Context){

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val IS_USER_SETUP = booleanPreferencesKey("is_user_setup")
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    val isLoggedIn = context.dataStore.data.map { it[IS_LOGGED_IN] ?: false }
    val isUserSetup = context.dataStore.data.map { it[IS_USER_SETUP] ?: false }

    val themeMode = context.dataStore.data.map { prefs ->
        ThemeMode.entries.find { it.name == prefs[THEME_MODE] } ?: ThemeMode.SYSTEM
    }


    suspend fun setLoggedIn(value: Boolean) {
        context.dataStore.edit { it[IS_LOGGED_IN] = value }
    }
    suspend fun setUserSetup(value: Boolean) {
        context.dataStore.edit {
            it[IS_USER_SETUP] = value
        }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[THEME_MODE] = mode.name }
    }
}