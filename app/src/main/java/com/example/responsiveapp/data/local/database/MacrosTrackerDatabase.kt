package com.example.responsiveapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.responsiveapp.data.local.converter.Converters
import com.example.responsiveapp.data.local.dao.FoodDetailDao
import com.example.responsiveapp.data.local.dao.FoodItemDao
import com.example.responsiveapp.data.local.dao.FoodLogDao
import com.example.responsiveapp.data.local.entity.FoodDetailEntity
import com.example.responsiveapp.data.local.entity.FoodItemEntity
import com.example.responsiveapp.data.local.entity.FoodLogEntity

@Database(
    entities = [
        FoodItemEntity::class,
        FoodDetailEntity::class,
        FoodLogEntity::class,
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class MacrosTrackerDatabase : RoomDatabase() {

    abstract fun foodItemDao(): FoodItemDao
    abstract fun foodDetailDao(): FoodDetailDao
    abstract fun foodLogDao(): FoodLogDao

    companion object {
        const val DATABASE_NAME = "macros_tracker_db"
    }
}