package com.example.responsiveapp.core.di

import android.content.Context
import androidx.room.Room
import com.example.responsiveapp.data.local.dao.FoodDetailDao
import com.example.responsiveapp.data.local.dao.FoodItemDao
import com.example.responsiveapp.data.local.dao.FoodLogDao
import com.example.responsiveapp.data.local.database.MacrosTrackerDatabase
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
        @ApplicationContext context: Context
    ): MacrosTrackerDatabase {
        return Room.databaseBuilder(
            context,
            MacrosTrackerDatabase::class.java,
            MacrosTrackerDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideFoodItemDao(database: MacrosTrackerDatabase): FoodItemDao =
        database.foodItemDao()

    @Provides
    @Singleton
    fun provideFoodDetailDao(database: MacrosTrackerDatabase): FoodDetailDao =
        database.foodDetailDao()

    @Provides
    @Singleton
    fun provideFoodLogDao(database: MacrosTrackerDatabase): FoodLogDao =
        database.foodLogDao()
}