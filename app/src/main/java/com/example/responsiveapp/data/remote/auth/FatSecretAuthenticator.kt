package com.example.responsiveapp.data.remote.auth

import android.util.Log
import com.example.responsiveapp.core.constant.Constants.LOG
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class FatSecretAuthenticator @Inject constructor(
    private val tokenManager: TokenManager
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        if (responseCount(response) >= 2) {
            return null
        }
        val newToken = runBlocking {
            tokenManager.refreshToken()

        }
        if (newToken == null) {
            return null
        }
        return response.request.newBuilder()
            .header("Authorization", "Bearer $newToken")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var currentResponse = response.priorResponse
        while (currentResponse != null) {
            result++
            currentResponse = currentResponse.priorResponse
        }
        return result
    }
}