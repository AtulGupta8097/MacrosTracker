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
import com.example.responsiveapp.data.local.entity.CustomFoodEntity
import com.example.responsiveapp.data.local.entity.DailySummaryEntity
import com.example.responsiveapp.data.local.entity.FoodDetailEntity
import com.example.responsiveapp.data.local.entity.FoodItemEntity
import com.example.responsiveapp.data.local.entity.FoodLogEntity
import com.example.responsiveapp.data.local.entity.MacroTargetEntity
import com.example.responsiveapp.data.local.entity.MyMealsEntity
import com.example.responsiveapp.data.local.entity.SearchQueryEntity
import com.example.responsiveapp.data.local.entity.SearchResultCrossRef

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
        MacroTargetEntity::class
    ],
    version = 9,
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

    companion object {
        const val DATABASE_NAME = "macros_tracker_db"

        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {

                db.execSQL("""
                    CREATE TABLE macro_targets_new (
                        id                  TEXT    NOT NULL PRIMARY KEY,
                        target_calories     INTEGER NOT NULL,
                        target_protein      INTEGER NOT NULL,
                        target_carbs        INTEGER NOT NULL,
                        target_fats         INTEGER NOT NULL,
                        target_fiber        INTEGER NOT NULL,
                        target_sugarLimit   INTEGER NOT NULL,
                        target_sodiumLimit  INTEGER NOT NULL,
                        bmr                 INTEGER NOT NULL,
                        tdee                INTEGER NOT NULL,
                        createdAt           INTEGER NOT NULL,
                        syncStatus          TEXT    NOT NULL DEFAULT 'PENDING',
                        retryCount          INTEGER NOT NULL DEFAULT 0,
                        lastSyncAttempt     INTEGER
                    )
                """)

                db.execSQL("""
                    INSERT INTO macro_targets_new (
                        id, target_calories, target_protein, target_carbs, target_fats,
                        target_fiber, target_sugarLimit, target_sodiumLimit,
                        bmr, tdee, createdAt, syncStatus, retryCount, lastSyncAttempt
                    )
                    SELECT
                        id, calories, protein, carbs, fats,
                        fiber, sugarLimit, sodiumLimit,
                        bmr, tdee, createdAt, syncStatus, retryCount, lastSyncAttempt
                    FROM macro_targets
                """)

                db.execSQL("DROP TABLE macro_targets")
                db.execSQL("ALTER TABLE macro_targets_new RENAME TO macro_targets")

                // food_logs
                db.execSQL("""
                    CREATE TABLE food_logs_new (
                        id                  TEXT    NOT NULL PRIMARY KEY,
                        date                INTEGER NOT NULL,
                        foodName            TEXT    NOT NULL,
                        servingDescription  TEXT    NOT NULL,
                        quantity            REAL    NOT NULL,
                        nutrition_calories          REAL    NOT NULL DEFAULT 0,
                        nutrition_protein           REAL    NOT NULL DEFAULT 0,
                        nutrition_carbs             REAL    NOT NULL DEFAULT 0,
                        nutrition_fat               REAL    NOT NULL DEFAULT 0,
                        nutrition_fiber             REAL    NOT NULL DEFAULT 0,
                        nutrition_sugar             REAL    NOT NULL DEFAULT 0,
                        nutrition_sodium            REAL    NOT NULL DEFAULT 0,
                        nutrition_cholesterol       REAL    NOT NULL DEFAULT 0,
                        nutrition_saturatedFat      REAL    NOT NULL DEFAULT 0,
                        nutrition_polyunsaturatedFat REAL   NOT NULL DEFAULT 0,
                        nutrition_monounsaturatedFat REAL   NOT NULL DEFAULT 0,
                        nutrition_transFat          REAL    NOT NULL DEFAULT 0,
                        nutrition_potassium         REAL    NOT NULL DEFAULT 0,
                        nutrition_addedSugars       REAL    NOT NULL DEFAULT 0,
                        nutrition_vitaminA          REAL    NOT NULL DEFAULT 0,
                        nutrition_vitaminC          REAL    NOT NULL DEFAULT 0,
                        nutrition_vitaminD          REAL    NOT NULL DEFAULT 0,
                        nutrition_calcium           REAL    NOT NULL DEFAULT 0,
                        nutrition_iron              REAL    NOT NULL DEFAULT 0,
                        ingredientsJson     TEXT,
                        createdAt           INTEGER NOT NULL,
                        updatedAt           INTEGER NOT NULL,
                        syncStatus          TEXT    NOT NULL DEFAULT 'PENDING',
                        retryCount          INTEGER NOT NULL DEFAULT 0,
                        lastSyncAttempt     INTEGER
                    )
                """)

                db.execSQL("""
                    INSERT INTO food_logs_new (
                        id, date, foodName, servingDescription, quantity,
                        nutrition_calories, nutrition_protein, nutrition_carbs, nutrition_fat,
                        nutrition_fiber, nutrition_sugar, nutrition_sodium, nutrition_cholesterol,
                        nutrition_saturatedFat, nutrition_polyunsaturatedFat, nutrition_monounsaturatedFat,
                        nutrition_transFat, nutrition_potassium, nutrition_addedSugars,
                        nutrition_vitaminA, nutrition_vitaminC, nutrition_vitaminD,
                        nutrition_calcium, nutrition_iron,
                        ingredientsJson, createdAt, updatedAt, syncStatus, retryCount, lastSyncAttempt
                    )
                    SELECT
                        id, date, foodName, servingDescription, quantity,
                        nutrition_calories, nutrition_protein, nutrition_carbs, nutrition_fat,
                        nutrition_fiber, nutrition_sugar, nutrition_sodium, nutrition_cholesterol,
                        nutrition_saturatedFat, nutrition_polyunsaturatedFat, nutrition_monounsaturatedFat,
                        nutrition_transFat, nutrition_potassium, nutrition_addedSugars,
                        nutrition_vitaminA, nutrition_vitaminC, nutrition_vitaminD,
                        nutrition_calcium, nutrition_iron,
                        ingredientsJson, createdAt, updatedAt, syncStatus, retryCount, lastSyncAttempt
                    FROM food_logs
                """)

                db.execSQL("DROP TABLE food_logs")
                db.execSQL("ALTER TABLE food_logs_new RENAME TO food_logs")
            }
        }
    }
}