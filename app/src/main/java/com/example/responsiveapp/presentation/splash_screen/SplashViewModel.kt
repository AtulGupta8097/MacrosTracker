package com.example.responsiveapp.presentation.splash_screen

import androidx.lifecycle.ViewModel
import com.example.responsiveapp.core.navigation.Routes
import com.example.responsiveapp.data.datastore.AppPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val prefs: AppPrefManager
) : ViewModel() {

    val startDestination: Flow<Routes> =
        combine(
            prefs.isLoggedIn,
            prefs.isUserSetup
        ) { isLoggedIn, isUserSetup ->

            when {
                !isLoggedIn -> Routes.SignInScreen
                isLoggedIn && !isUserSetup -> Routes.UserSetupScreen
                else -> Routes.MainScreen
            }
        }
}
