package com.example.responsiveapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.responsiveapp.data.local.converter.IngredientConverters
import com.example.responsiveapp.data.local.converter.NutritionInfoConverter
import com.example.responsiveapp.data.local.converter.ServingConverter
import com.example.responsiveapp.data.local.converter.SyncStatusConverter
import com.example.responsiveapp.data.local.dao.CustomFoodDao
import com.example.responsiveapp.data.local.dao.DailySummaryDao
import com.example.responsiveapp.data.local.dao.FoodDetailDao
import com.example.responsiveapp.data.local.dao.FoodLogDao
import com.example.responsiveapp.data.local.dao.FoodSearchDao
import com.example.responsiveapp.data.local.dao.MacroTargetDao
import com.example.responsiveapp.data.local.dao.MyMealsDao
import com.example.responsiveapp.data.local.dao.UserProfileDao
import com.example.responsiveapp.data.local.entity.CustomFoodEntity
import com.example.responsiveapp.data.local.entity.DailySummaryEntity
import com.example.responsiveapp.data.local.entity.FoodDetailEntity
import com.example.responsiveapp.data.local.entity.FoodItemEntity
import com.example.responsiveapp.data.local.entity.FoodLogEntity
import com.example.responsiveapp.data.local.entity.MacroTargetEntity
import com.example.responsiveapp.data.local.entity.MyMealsEntity
import com.example.responsiveapp.data.local.entity.SearchQueryEntity
import com.example.responsiveapp.data.local.entity.SearchResultCrossRef
import com.example.responsiveapp.data.local.entity.UserProfileEntity

@Database(
    entities = [
        FoodItemEntity::class,
        FoodDetailEntity::class,
        FoodLogEntity::class,
        MyMealsEntity::class,
        DailySummaryEntity::class,
        CustomFoodEntity::class,
        SearchQueryEntity::class,
        SearchResultCrossRef::class,
        MacroTargetEntity::class,
        UserProfileEntity::class
    ],
    version = 10,
    exportSchema = false,
)
@TypeConverters(
    ServingConverter::class,
    NutritionInfoConverter::class,
    IngredientConverters::class,
    SyncStatusConverter::class
)
abstract class MacrosTrackerDatabase : RoomDatabase() {

    abstract fun foodDetailDao(): FoodDetailDao
    abstract fun foodLogDao(): FoodLogDao
    abstract fun myMealsDAo(): MyMealsDao
    abstract fun customFoodDao(): CustomFoodDao
    abstract fun foodSearchDao(): FoodSearchDao
    abstract fun macroTargetDao(): MacroTargetDao
    abstract fun dailySummaryDao(): DailySummaryDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        const val DATABASE_NAME = "macros_tracker_db"

    }
}