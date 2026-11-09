package com.macrivo.app.core.di

import android.content.Context
import androidx.room.Room
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
import com.macrivo.app.data.local.database.MacrosTrackerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): MacrosTrackerDatabase =
        Room.databaseBuilder(
            context,
            MacrosTrackerDatabase::class.java,
            MacrosTrackerDatabase.DATABASE_NAME,
        )
            .fallbackToDestructiveMigration(false)
            .build()

    @Provides
    @Singleton
    fun provideFoodDetailDao(database: MacrosTrackerDatabase): FoodDetailDao =
        database.foodDetailDao()

    @Provides
    @Singleton
    fun provideFoodLogDao(database: MacrosTrackerDatabase): FoodLogDao =
        database.foodLogDao()

    @Provides
    @Singleton
    fun provideMyMealsDao(database: MacrosTrackerDatabase): MyMealsDao =
        database.myMealsDAo()

    @Provides
    @Singleton
    fun provideCustomFoodDao(database: MacrosTrackerDatabase): CustomFoodDao =
        database.customFoodDao()

    @Provides
    @Singleton
    fun provideFoodSearchDao(database: MacrosTrackerDatabase): FoodSearchDao =
        database.foodSearchDao()

    @Provides
    @Singleton
    fun provideMacroTargetDao(
        database: MacrosTrackerDatabase
    ): MacroTargetDao =
        database.macroTargetDao()

    @Provides
    @Singleton
    fun provideDailySummaryDao(
        database: MacrosTrackerDatabase
    ): DailySummaryDao =
        database.dailySummaryDao()

    @Provides
    @Singleton
    fun provideUserProfileDao(
        database: MacrosTrackerDatabase
    ): UserProfileDao =
        database.userProfileDao()

    @Provides
    @Singleton
    fun provideWeightLogDao(
        database: MacrosTrackerDatabase
    ): WeightLogDao =
        database.weightLogDao()

    @Provides
    @Singleton
    fun provideSavedFoodDao(
        database: MacrosTrackerDatabase
    ): SavedFoodDao =
        database.savedFoodDao()
}