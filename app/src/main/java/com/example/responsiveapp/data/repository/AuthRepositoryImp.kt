package com.example.responsiveapp.data.repository

import com.example.responsiveapp.data.mapper.toAuthException
import com.example.responsiveapp.domain.model.auth.AuthException
import com.example.responsiveapp.domain.model.auth.AuthFailure
import com.example.responsiveapp.domain.model.auth.LinkedProviders
import com.example.responsiveapp.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImp(private val auth: FirebaseAuth) : AuthRepository {

    private class PendingGoogleLink(val email: String?, val credential: AuthCredential)

    @Volatile
    private var pendingGoogleLink: PendingGoogleLink? = null

    override suspend fun signIn(
        email: String,
        password: String
    ): Flow<Result<AuthResult>> = authFlow {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        linkPendingGoogleCredential(result.user)
        result
    }

    override suspend fun signUp(
        email: String,
        password: String,
        name: String
    ): Flow<Result<AuthResult>> = authFlow {
        val result = auth.createUserWithEmailAndPassword(email, password).await()

        try {
            result.user?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(name.trim())
                    .build()
            )?.await()
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
        }

        result
    }

    override suspend fun signInWithGoogle(idToken: String): Flow<Result<AuthResult>> = authFlow {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        try {
            auth.signInWithCredential(credential).await()
        } catch (e: FirebaseAuthUserCollisionException) {
            pendingGoogleLink = PendingGoogleLink(e.email, credential)
            throw AuthException(AuthFailure.AccountConflict(e.email), e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Flow<Result<Unit>> = authFlow {
        auth.sendPasswordResetEmail(email).await()
    }

    override suspend fun deleteAccount(): Flow<Result<Unit>> = flow {
        try {
            val user = auth.currentUser
                ?: throw IllegalStateException("No signed-in user")
            user.delete().await()
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): Result<Unit> = runAuth {
        val user = auth.currentUser ?: throw AuthException(AuthFailure.NotSignedIn)
        val email = user.email ?: throw AuthException(AuthFailure.NotSignedIn)
        user.reauthenticate(EmailAuthProvider.getCredential(email, currentPassword)).await()
        user.updatePassword(newPassword).await()
    }

    override suspend fun reauthenticateWithPassword(password: String): Result<Unit> = runAuth {
        val user = auth.currentUser ?: throw AuthException(AuthFailure.NotSignedIn)
        val email = user.email ?: throw AuthException(AuthFailure.NotSignedIn)
        user.reauthenticate(EmailAuthProvider.getCredential(email, password)).await()

    }

    override suspend fun reauthenticateWithGoogle(idToken: String): Result<Unit> = runAuth {
        val user = auth.currentUser ?: throw AuthException(AuthFailure.NotSignedIn)
        user.reauthenticate(GoogleAuthProvider.getCredential(idToken, null)).await()

    }

    override fun getLinkedProviders(): LinkedProviders {
        val ids = auth.currentUser?.providerData?.map { it.providerId }.orEmpty()
        return LinkedProviders(
            hasPassword = EmailAuthProvider.PROVIDER_ID in ids,
            hasGoogle = GoogleAuthProvider.PROVIDER_ID in ids,
        )
    }

    private suspend fun linkPendingGoogleCredential(user: FirebaseUser?) {
        val pending = pendingGoogleLink ?: return
        pendingGoogleLink = null
        if (user == null || !user.email.equals(pending.email, ignoreCase = true)) return
        try {
            user.linkWithCredential(pending.credential).await()
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
        }
    }

    private fun <T> authFlow(block: suspend () -> T): Flow<Result<T>> = flow {
        emit(runAuth(block))
    }

    private suspend fun <T> runAuth(block: suspend () -> T): Result<T> =
        try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e.toAuthException())
        }
}
