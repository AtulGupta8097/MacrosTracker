package com.macrivo.app.domain.use_case.authentication

import com.macrivo.app.data.datastore.AppPrefManager
import com.macrivo.app.data.local.database.MacrosTrackerDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ClaimLocalDataUseCase @Inject constructor(
    private val appPrefManager: AppPrefManager,
    private val database: MacrosTrackerDatabase,
) {
    suspend operator fun invoke(uid: String) {
        if (appPrefManager.lastUserId() != uid) {
            withContext(Dispatchers.IO) { database.clearAllTables() }
        }
        appPrefManager.setLastUserId(uid)
    }
}
