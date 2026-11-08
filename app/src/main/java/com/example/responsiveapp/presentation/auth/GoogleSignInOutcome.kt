package com.example.responsiveapp.presentation.auth

import com.example.responsiveapp.domain.use_case.authentication.CompleteSignInUseCase
import com.example.responsiveapp.domain.use_case.authentication.SignInWithGoogleUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

sealed interface GoogleSignInOutcome {
    data class Success(val isUserSetupComplete: Boolean) : GoogleSignInOutcome
    data object Cancelled : GoogleSignInOutcome
    data class Failure(val message: String) : GoogleSignInOutcome
}

class GoogleSignInHandler @Inject constructor(
    private val signInWithGoogle: SignInWithGoogleUseCase,
    private val completeSignIn: CompleteSignInUseCase,
) {
    suspend operator fun invoke(fetchToken: suspend () -> GoogleTokenResult): GoogleSignInOutcome {
        val result = fetchToken()
        val idToken = when (result) {
            is GoogleTokenResult.Success -> result.idToken
            GoogleTokenResult.Cancelled -> return GoogleSignInOutcome.Cancelled
            else -> return GoogleSignInOutcome.Failure(
                result.failureMessage() ?: "Google sign-in didn't complete. Please try again."
            )
        }

        return signInWithGoogle(idToken).first().fold(
            onSuccess = { GoogleSignInOutcome.Success(completeSignIn()) },
            onFailure = { GoogleSignInOutcome.Failure(it.toUserMessage()) },
        )
    }
}
