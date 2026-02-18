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
    object UserSetupScreen : Routes, NavKey
    @Serializable
    object MainScreen : Routes, NavKey
    @Serializable
    object HomeScreen: Routes, NavKey
    @Serializable
    object ProgressScreen: Routes, NavKey
    @Serializable
    object GoalsScreen: Routes, NavKey
    @Serializable
    object SettingsScreen: Routes, NavKey
    @Serializable
    object FoodDatabaseScreen: Routes, NavKey
    @Serializable
    data class FoodDetailScreen(val foodId: String): Routes, NavKey

}
