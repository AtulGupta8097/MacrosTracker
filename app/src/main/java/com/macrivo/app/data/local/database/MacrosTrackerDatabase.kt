package com.macrivo.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.macrivo.app.data.local.converter.IngredientConverters
import com.macrivo.app.data.local.converter.NutritionInfoConverter
import com.macrivo.app.data.local.converter.ServingConverter
import com.macrivo.app.data.local.converter.SyncStatusConverter
import com.macrivo.app.data.local.dao.CustomFoodDao
import com.macrivo.app.data.local.dao.DailySummaryDao
import com.macrivo.app.data.local.dao.FoodDetailDao
import com.macrivo.app.data.local.dao.FoodLogDao
import com.macrivo.app.data.local.dao.FoodSearchDao
import com.macrivo.app.data.local.dao.MacroTargetDao
import com.macrivo.app.data.local.dao.MyMealsDao
import com.macrivo.app.data.local.dao.SavedFoodDao
import com.macrivo.app.data.local.dao.UserProfileDao
import com.macrivo.app.data.local.dao.WeightLogDao
import com.macrivo.app.data.local.entity.CustomFoodEntity
import com.macrivo.app.data.local.entity.DailySummaryEntity
import com.macrivo.app.data.local.entity.FoodDetailEntity
import com.macrivo.app.data.local.entity.FoodItemEntity
import com.macrivo.app.data.local.entity.FoodLogEntity
import com.macrivo.app.data.local.entity.MacroTargetEntity
import com.macrivo.app.data.local.entity.MyMealsEntity
import com.macrivo.app.data.local.entity.SavedFoodEntity
import com.macrivo.app.data.local.entity.SearchQueryEntity
import com.macrivo.app.data.local.entity.SearchResultCrossRef
import com.macrivo.app.data.local.entity.UserProfileEntity
import com.macrivo.app.data.local.entity.WeightLogEntity

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
        UserProfileEntity::class,
        WeightLogEntity::class,
        SavedFoodEntity::class,
    ],
    version = 13,
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

    abstract fun weightLogDao(): WeightLogDao
    abstract fun savedFoodDao(): SavedFoodDao


    companion object {
        const val DATABASE_NAME = "macros_tracker_db"

    }
}