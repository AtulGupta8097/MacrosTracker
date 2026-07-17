package com.example.responsiveapp.sync

interface SyncManager {

    suspend fun sync(syncType: SyncType)
}
