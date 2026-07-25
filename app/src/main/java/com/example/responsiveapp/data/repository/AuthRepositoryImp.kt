package com.example.responsiveapp.data.repository

import android.R.attr.name
import com.example.responsiveapp.domain.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImp(private val auth: FirebaseAuth) : AuthRepository {

    override suspend fun signIn(
        email: String,
        password: String
    ): Flow<Result<AuthResult>> = flow {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            emit(Result.success(result))
        }
        catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        name: String
    ): Flow<Result<AuthResult>> = flow {
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()

            result.user?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(name.trim())
                    .build()
            )?.await()

            emit(Result.success(result))
        }
        catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

}