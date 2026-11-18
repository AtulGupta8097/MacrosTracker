package com.macrivo.app.domain.use_case.authentication

import androidx.work.WorkManager
import com.macrivo.app.data.datastore.AppPrefManager
import com.macrivo.app.data.local.database.MacrosTrackerDatabase
import com.macrivo.app.domain.session.SessionManager
import com.macrivo.app.sync.SyncManager
import com.macrivo.app.sync.SyncType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class SignOutUseCase @Inject constructor(
    private val sessionManager: SessionManager,
    private val appPrefManager: AppPrefManager,
    private val database: MacrosTrackerDatabase,
    private val syncManager: SyncManager,
    private val workManager: WorkManager,
) {
    suspend operator fun invoke() {

        flushPendingSync()

        workManager.cancelAllWork()

        sessionManager.signOut()

        withContext(Dispatchers.IO) {
            database.clearAllTables()
        }

        appPrefManager.setLoggedIn(false)
        appPrefManager.setUserSetup(false)

    }

    private suspend fun flushPendingSync() {
        withTimeoutOrNull(FLUSH_TIMEOUT_MS.milliseconds) {
            SyncType.entries.forEach { type ->
                try {
                    syncManager.sync(type)
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    //  a failed type must not stop the others
                }
            }
        }
    }

    private companion object {
        const val FLUSH_TIMEOUT_MS = 6_000L
    }
}
