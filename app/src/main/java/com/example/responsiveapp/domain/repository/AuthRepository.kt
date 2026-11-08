package com.example.responsiveapp.domain.repository

import com.example.responsiveapp.domain.model.auth.LinkedProviders
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signIn(email: String, password: String): Flow<Result<AuthResult>>

    suspend fun signUp(email: String, password: String, name: String): Flow<Result<AuthResult>>

    suspend fun signInWithGoogle(idToken: String): Flow<Result<AuthResult>>

    suspend fun sendPasswordResetEmail(email: String): Flow<Result<Unit>>

    suspend fun deleteAccount(): Flow<Result<Unit>>

    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit>

    suspend fun reauthenticateWithPassword(password: String): Result<Unit>

    suspend fun reauthenticateWithGoogle(idToken: String): Result<Unit>

    fun getLinkedProviders(): LinkedProviders
}
