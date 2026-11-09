package com.macrivo.app.sync

interface SyncScheduler {

    fun schedule(syncType: SyncType)

}