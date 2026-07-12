package com.example.responsiveapp.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncSchedulerImpl @Inject constructor(
    @field:ApplicationContext
    private val context: Context
) : SyncScheduler {

    override fun schedule(syncType: SyncType) {

        val request =
            OneTimeWorkRequestBuilder<SyncWorker>()
                .setInputData(
                    workDataOf(
                        SyncConstants.KEY_SYNC_TYPE to syncType.name
                    )
                )
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(
                SyncConstants.UNIQUE_WORK,
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                request
            )
    }
}