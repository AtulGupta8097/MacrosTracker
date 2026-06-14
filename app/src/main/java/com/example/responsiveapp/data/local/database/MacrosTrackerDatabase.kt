package com.example.responsiveapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.responsiveapp.data.local.converter.IngredientConverters
import com.example.responsiveapp.data.local.converter.NutritionInfoConverter
import com.example.responsiveapp.data.local.converter.ServingConverter
import com.example.responsiveapp.data.local.dao.CustomFoodDao
import com.example.responsiveapp.data.local.dao.FoodDetailDao
import com.example.responsiveapp.data.local.dao.FoodLogDao
import com.example.responsiveapp.data.local.dao.FoodSearchDao
import com.example.responsiveapp.data.local.dao.MyMealsDao
import com.example.responsiveapp.data.local.entity.CustomFoodEntity
import com.example.responsiveapp.data.local.entity.FoodDetailEntity
import com.example.responsiveapp.data.local.entity.FoodItemEntity
import com.example.responsiveapp.data.local.entity.FoodLogEntity
import com.example.responsiveapp.data.local.entity.MyMealsEntity
import com.example.responsiveapp.data.local.entity.SearchQueryEntity
import com.example.responsiveapp.data.local.entity.SearchResultCrossRef

@Database(
    entities = [
        FoodItemEntity::class,
        FoodDetailEntity::class,
        FoodLogEntity::class,
        MyMealsEntity::class,
        CustomFoodEntity::class,
        SearchQueryEntity::class,
        SearchResultCrossRef::class,
    ],
    version      = 6,
    exportSchema = false,
)
@TypeConverters(
    ServingConverter::class,
    NutritionInfoConverter::class,
    IngredientConverters::class,
)
abstract class MacrosTrackerDatabase : RoomDatabase() {

    abstract fun foodDetailDao() : FoodDetailDao
    abstract fun foodLogDao()    : FoodLogDao
    abstract fun myMealsDAo()    : MyMealsDao
    abstract fun customFoodDao() : CustomFoodDao
    abstract fun foodSearchDao() : FoodSearchDao

    companion object {
        const val DATABASE_NAME = "macros_tracker_db"
    }
}