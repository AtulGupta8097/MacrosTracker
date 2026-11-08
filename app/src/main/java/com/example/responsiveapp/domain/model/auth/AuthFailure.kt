package com.example.responsiveapp.domain.model.auth

sealed interface AuthFailure {
    data object InvalidEmail : AuthFailure
    data object InvalidCredentials : AuthFailure
    data object WeakPassword : AuthFailure
    data object EmailAlreadyInUse : AuthFailure
    data class AccountConflict(val email: String?) : AuthFailure
    data object RequiresRecentLogin : AuthFailure
    data object UserDisabled : AuthFailure
    data object Network : AuthFailure
    data object TooManyRequests : AuthFailure
    data object Cancelled : AuthFailure
    data object NotSignedIn : AuthFailure
    data object GoogleNotConfigured : AuthFailure
    data object AccountDeletionIncomplete : AuthFailure
    data object CloudDataDeletionFailed : AuthFailure
    data object Unknown : AuthFailure
}

class AuthException(
    val failure: AuthFailure,
    cause: Throwable? = null,
) : Exception(failure.toString(), cause)

data class LinkedProviders(
    val hasPassword: Boolean = false,
    val hasGoogle: Boolean = false,
)
