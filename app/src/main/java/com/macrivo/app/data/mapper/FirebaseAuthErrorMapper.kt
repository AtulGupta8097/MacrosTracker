package com.macrivo.app.data.mapper

import com.macrivo.app.domain.model.auth.AuthException
import com.macrivo.app.domain.model.auth.AuthFailure
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

fun Throwable.toAuthException(): AuthException = when (this) {
    is AuthException -> this
    is FirebaseAuthWeakPasswordException -> AuthException(AuthFailure.WeakPassword, this)
    is FirebaseAuthInvalidCredentialsException -> when (errorCode) {
        "ERROR_INVALID_EMAIL" -> AuthException(AuthFailure.InvalidEmail, this)
        else -> AuthException(AuthFailure.InvalidCredentials, this)
    }
    is FirebaseAuthInvalidUserException -> when (errorCode) {
        "ERROR_USER_DISABLED" -> AuthException(AuthFailure.UserDisabled, this)
        else -> AuthException(AuthFailure.InvalidCredentials, this) // unknown email looks like a wrong password
    }
    is FirebaseAuthUserCollisionException -> AuthException(AuthFailure.EmailAlreadyInUse, this)
    is FirebaseAuthRecentLoginRequiredException -> AuthException(AuthFailure.RequiresRecentLogin, this)
    is FirebaseNetworkException -> AuthException(AuthFailure.Network, this)
    is FirebaseTooManyRequestsException -> AuthException(AuthFailure.TooManyRequests, this)
    else -> AuthException(AuthFailure.Unknown, this)
}
