package com.example.responsiveapp.data.local.converter

import androidx.room.TypeConverter
import com.example.responsiveapp.domain.model.SyncStatus

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