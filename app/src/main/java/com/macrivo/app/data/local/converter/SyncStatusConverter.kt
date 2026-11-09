package com.macrivo.app.data.local.converter

import androidx.room.TypeConverter
import com.macrivo.app.domain.model.SyncStatus

class SyncStatusConverter {

    @TypeConverter
    fun fromSyncStatus(status: SyncStatus): String {
        return status.name
    }

    @TypeConverter
    fun toSyncStatus(value: String): SyncStatus {
        return SyncStatus.valueOf(value)
    }
}