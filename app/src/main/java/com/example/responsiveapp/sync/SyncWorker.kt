package com.example.responsiveapp.sync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(

    @Assisted
    context: Context,

    @Assisted
    params: WorkerParameters,

    private val syncManager: SyncManager

) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val syncTypeName = inputData.getString(
            SyncConstants.KEY_SYNC_TYPE
        ) ?: return Result.failure()

        val syncType = try {
            SyncType.valueOf(syncTypeName)
        } catch (e: IllegalArgumentException) {

            Log.e(
                TAG,
                "Unknown SyncType: $syncTypeName",
                e
            )

            return Result.failure()
        }

        return try {

            syncManager.sync(syncType)

            Result.success()

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Unexpected error while syncing $syncType",
                e
            )

            Result.failure()
        }
    }

    companion object {
        private const val TAG = "SyncWorker"
    }
}