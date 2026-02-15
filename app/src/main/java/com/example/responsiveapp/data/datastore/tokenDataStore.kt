package com.example.responsiveapp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "encrypted_token_prefs"
)

interface TokenDataStore {
    suspend fun saveToken(token: String, expiryTime: Long)
    suspend fun getToken(): String?
    suspend fun getTokenExpiry(): Long
    suspend fun clearToken()
    fun tokenFlow(): Flow<String?>
    fun tokenExpiryFlow(): Flow<Long>
}

@Singleton
class EncryptedTokenDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenDataStore {
    
    private val dataStore = context.tokenDataStore
    
    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_TOKEN_EXPIRY = longPreferencesKey("token_expiry")
    }
    
    override suspend fun saveToken(token: String, expiryTime: Long) {
        dataStore.edit { preferences ->
            preferences[KEY_ACCESS_TOKEN] = token
            preferences[KEY_TOKEN_EXPIRY] = expiryTime
        }
    }
    
    override suspend fun getToken(): String? {
        return dataStore.data
            .map { preferences -> preferences[KEY_ACCESS_TOKEN] }
            .firstOrNull()
    }
    
    override suspend fun getTokenExpiry(): Long {
        return dataStore.data
            .map { preferences -> preferences[KEY_TOKEN_EXPIRY] ?: 0L }
            .first()
    }
    
    override suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_ACCESS_TOKEN)
            preferences.remove(KEY_TOKEN_EXPIRY)
        }
    }
    
    override fun tokenFlow(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[KEY_ACCESS_TOKEN]
        }
    }
    
    override fun tokenExpiryFlow(): Flow<Long> {
        return dataStore.data.map { preferences ->
            preferences[KEY_TOKEN_EXPIRY] ?: 0L
        }
    }
}