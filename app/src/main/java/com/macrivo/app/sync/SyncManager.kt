package com.macrivo.app.sync

interface SyncManager {

    suspend fun sync(syncType: SyncType)
}
