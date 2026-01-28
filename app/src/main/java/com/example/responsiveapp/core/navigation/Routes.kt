package com.example.responsiveapp.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Routes : NavKey {
    @Serializable
    object SplashScreen : Routes, NavKey
    @Serializable
    object SignInScreen : Routes, NavKey
    @Serializable
    object SignUpScreen : Routes, NavKey
    @Serializable
    object MainScreen : Routes, NavKey

    @Serializable
    object UserSetupScreen : Routes, NavKey {
        @Serializable
        object GenderScreen : Routes, NavKey
        @Serializable
        object AgeScreen : Routes, NavKey
    }

}
