package com.example.responsiveapp.data.remote.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class FatSecretAuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val isTokenEndpoint = originalRequest.url.encodedPath.contains("/connect/token")

        if (isTokenEndpoint) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking {
            try {
                tokenManager.getValidToken()
            } catch (e: Exception) {
                null
            }
        }

        if (token == null) {
            throw IOException("Failed to obtain access token. Check your FatSecret API credentials and network connection.")
        }

        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}