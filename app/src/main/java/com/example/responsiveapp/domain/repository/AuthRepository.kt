package com.example.responsiveapp.domain.repository

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signIn(email: String, password: String): Flow<Result<AuthResult>>

    suspend fun signUp(email: String, password: String): Flow<Result<AuthResult>>

}