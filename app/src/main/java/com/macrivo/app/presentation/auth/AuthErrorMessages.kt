package com.macrivo.app.presentation.auth

import com.macrivo.app.domain.model.auth.AuthException
import com.macrivo.app.domain.model.auth.AuthFailure

fun AuthFailure.toUserMessage(
    invalidCredentialsMessage: String = "Incorrect email or password. Please try again.",
): String = when (this) {
    AuthFailure.InvalidEmail -> "That email address doesn't look right. Check it and try again."
    AuthFailure.InvalidCredentials -> invalidCredentialsMessage
    AuthFailure.WeakPassword ->
        "Choose a stronger password: at least 8 characters with a letter and a number."
    AuthFailure.EmailAlreadyInUse ->
        "An account with this email already exists. Sign in instead, or continue with Google."
    is AuthFailure.AccountConflict ->
        "An account for ${email ?: "this email"} already exists. Sign in with your password once " +
            "and we'll link Google to it."
    AuthFailure.RequiresRecentLogin ->
        "For your security, please sign out, sign in again, and then retry."
    AuthFailure.UserDisabled -> "This account has been disabled. Please contact support."
    AuthFailure.Network -> "No internet connection. Check your connection and try again."
    AuthFailure.TooManyRequests -> "Too many attempts. Please wait a moment and try again."
    AuthFailure.Cancelled -> "Sign-in was cancelled."
    AuthFailure.NotSignedIn -> "You're signed out. Please sign in again."
    AuthFailure.GoogleNotConfigured -> "Google sign-in isn't set up for this build yet."
    AuthFailure.AccountDeletionIncomplete ->
        "Your online data was removed, but we couldn't finish closing your account. " +
            "Check your connection and try again."
    AuthFailure.CloudDataDeletionFailed ->
        "We couldn't delete your online data, so nothing was removed. " +
            "Check your connection and try again."
    AuthFailure.Unknown -> "Something went wrong. Please try again."
}

fun Throwable.toUserMessage(
    invalidCredentialsMessage: String = "Incorrect email or password. Please try again.",
): String =
    ((this as? AuthException)?.failure ?: AuthFailure.Unknown)
        .toUserMessage(invalidCredentialsMessage)

fun Throwable.authFailure(): AuthFailure =
    (this as? AuthException)?.failure ?: AuthFailure.Unknown
