package com.example.responsiveapp.domain.use_case.authentication

import com.example.responsiveapp.data.datastore.AppPrefManager
import com.example.responsiveapp.domain.session.SessionManager
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val sessionManager: SessionManager,
    private val appPrefManager: AppPrefManager,
) {
    suspend operator fun invoke() {
        sessionManager.signOut()
        appPrefManager.setLoggedIn(false)
    }
}
