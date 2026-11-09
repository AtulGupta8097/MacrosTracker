package com.macrivo.app.domain.use_case.authentication

import com.macrivo.app.data.datastore.AppPrefManager
import com.macrivo.app.data.local.database.MacrosTrackerDatabase
import com.macrivo.app.domain.session.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val sessionManager: SessionManager,
    private val appPrefManager: AppPrefManager,
    private val database: MacrosTrackerDatabase,
) {
    suspend operator fun invoke() {
        sessionManager.signOut()

        withContext(Dispatchers.IO) {
            database.clearAllTables()
        }

        appPrefManager.setLoggedIn(false)
        appPrefManager.setUserSetup(false)

    }
}
