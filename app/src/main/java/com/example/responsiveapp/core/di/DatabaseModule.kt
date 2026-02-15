package com.example.responsiveapp.di

import android.content.Context
import androidx.room.Room
import com.example.responsiveapp.data.local.dao.FoodDao
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
    fun provideFoodDao(database: MacrosTrackerDatabase): FoodDao {
        return database.foodDao()
    }

    @Provides
    @Singleton
    fun provideFoodLogDao(database: MacrosTrackerDatabase): FoodLogDao {
        return database.foodLogDao()
    }
}