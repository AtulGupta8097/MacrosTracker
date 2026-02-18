package com.example.responsiveapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.responsiveapp.data.local.converter.Converters
import com.example.responsiveapp.data.local.dao.FoodDao
import com.example.responsiveapp.data.local.dao.FoodLogDao
import com.example.responsiveapp.data.local.entity.FoodEntity
import com.example.responsiveapp.data.local.entity.FoodLogEntity

@Database(
    entities = [
        FoodEntity::class,
        FoodLogEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class MacrosTrackerDatabase : RoomDatabase() {

    abstract fun foodDao(): FoodDao
    abstract fun foodLogDao(): FoodLogDao

    companion object {
        const val DATABASE_NAME = "macros_tracker_db"
    }
}