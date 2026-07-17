package com.example.responsiveapp.sync

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncSchedulerImpl @Inject constructor(
    private val workManager: WorkManager
) : SyncScheduler {

    override fun schedule(syncType: SyncType) {

        val request = OneTimeWorkRequestBuilder<SyncWorker>()
            .setInputData(
                workDataOf(
                    SyncConstants.KEY_SYNC_TYPE to syncType.name
                )
            )
            .setConstraints(SYNC_CONSTRAINTS)
            .build()

        workManager.enqueueUniqueWork(
            SyncConstants.uniqueWorkNameFor(syncType),
            ExistingWorkPolicy.KEEP,
            request
        )
    }

    companion object {

        private val SYNC_CONSTRAINTS = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }
}