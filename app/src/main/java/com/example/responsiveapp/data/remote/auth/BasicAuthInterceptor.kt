package com.example.responsiveapp.data.remote.auth

import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor (
    private val clientId: String,
    private val clientSecret: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val isTokenEndpoint = originalRequest.url.encodedPath.contains("/connect/token")

        if (!isTokenEndpoint) {
            return chain.proceed(originalRequest)
        }

        val credentials = "$clientId:$clientSecret"
        val basicAuth = "Basic " + android.util.Base64.encodeToString(
            credentials.toByteArray(),
            android.util.Base64.NO_WRAP
        )

        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", basicAuth)
            .build()

        return chain.proceed(authenticatedRequest)
    }
}