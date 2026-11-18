package com.macrivo.app.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macrivo.app.core.navigation.Routes
import com.macrivo.app.data.datastore.AppPrefManager
import com.macrivo.app.domain.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class SplashViewModel @Inject constructor(
    prefs: AppPrefManager,
    private val sessionManager: SessionManager,
) : ViewModel() {

    val startDestination: StateFlow<Routes?> = flow {
        val isLoggedIn = prefs.isLoggedIn.first()
        val isUserSetup = prefs.isUserSetup.first()

        val hasFirebaseUser = sessionManager.currentUserId() != null

        emit(
            when {
                !hasFirebaseUser || !isLoggedIn -> Routes.SignInScreen
                !isUserSetup -> Routes.UserSetupScreen
                else -> Routes.MainScreen
            }
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
}
