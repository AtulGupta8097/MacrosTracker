package com.example.responsiveapp.data.remote.auth

interface TokenManager {
    suspend fun getValidToken(): String?
    suspend fun refreshToken(): String?
    suspend fun clearToken()
}