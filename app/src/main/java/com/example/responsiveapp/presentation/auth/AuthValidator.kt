package com.example.responsiveapp.presentation.auth

object AuthValidator {

    const val MIN_PASSWORD_LENGTH = 8
    private const val MAX_NAME_LENGTH = 50
    private val EMAIL = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$")

    fun email(raw: String): String? {
        val value = raw.trim()
        return when {
            value.isEmpty() -> "Enter your email address"
            !EMAIL.matches(value) -> "Enter a valid email address, like name@example.com"
            else -> null
        }
    }

    fun name(raw: String): String? {
        val value = raw.trim()
        return when {
            value.isEmpty() -> "Enter your name"
            value.length > MAX_NAME_LENGTH -> "Keep your name under $MAX_NAME_LENGTH characters"
            else -> null
        }
    }

    fun existingPassword(raw: String): String? =
        if (raw.isEmpty()) "Enter your password" else null

    fun newPassword(raw: String): String? = when {
        raw.isEmpty() -> "Create a password"
        raw.length < MIN_PASSWORD_LENGTH -> "Use at least $MIN_PASSWORD_LENGTH characters"
        raw.none { it.isLetter() } || raw.none { it.isDigit() } ->
            "Include at least one letter and one number"
        else -> null
    }

    fun confirmPassword(password: String, confirmation: String): String? = when {
        confirmation.isEmpty() -> "Re-enter your password"
        password != confirmation -> "Passwords don't match"
        else -> null
    }
}
