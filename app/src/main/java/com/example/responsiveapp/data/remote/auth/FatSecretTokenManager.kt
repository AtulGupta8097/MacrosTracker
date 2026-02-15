package com.example.responsiveapp.data.remote.auth

import com.example.responsiveapp.data.datastore.TokenDataStore
import com.example.responsiveapp.data.remote.api.FatSecretAuthApiService
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class FatSecretTokenManager(
    private val tokenDataStore: TokenDataStore,
    private val authApiService: FatSecretAuthApiService
) : TokenManager {

    private val mutex = Mutex()
    private var cachedToken: String? = null
    private var cachedExpiryTime: Long = 0

    override suspend fun getValidToken(): String? = mutex.withLock {

        if (cachedToken != null && System.currentTimeMillis() < cachedExpiryTime) {
            return cachedToken
        }

        val storedToken = tokenDataStore.getToken()
        val storedExpiry = tokenDataStore.getTokenExpiry()

        if (storedToken != null && System.currentTimeMillis() < storedExpiry) {
            cachedToken = storedToken
            cachedExpiryTime = storedExpiry
            return cachedToken
        }

        return refreshToken()
    }

    override suspend fun refreshToken(): String? {
        return try {
            val response = authApiService.getAccessToken()

            if (!response.isSuccessful) return null

            val body = response.body() ?: return null

            val token = body.accessToken
            val expiresIn = body.expiresIn

            val expiryTime =
                System.currentTimeMillis() + ((expiresIn - 300) * 1000)

            cachedToken = token
            cachedExpiryTime = expiryTime

            tokenDataStore.saveToken(token, expiryTime)

            token

        } catch (e: Exception) {
            null
        }
    }


    override suspend fun clearToken() {
        mutex.withLock {
            cachedToken = null
            cachedExpiryTime = 0
            tokenDataStore.clearToken()
        }
    }
}