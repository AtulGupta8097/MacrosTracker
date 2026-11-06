package com.example.responsiveapp.core.di

import com.example.responsiveapp.core.camera.CameraHelper
import com.example.responsiveapp.core.camera.CameraHelperImpl
import com.example.responsiveapp.data.coordinatior.LogFoodCoordinator
import com.example.responsiveapp.data.coordinatior.LogFoodCoordinatorImpl
import com.example.responsiveapp.sync.SyncManager
import com.example.responsiveapp.sync.SyncManagerImpl
import com.example.responsiveapp.sync.SyncScheduler
import com.example.responsiveapp.sync.SyncSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindScheduler(
        impl: SyncSchedulerImpl
    ): SyncScheduler

    @Binds
    abstract fun bindSyncManager(
        impl: SyncManagerImpl
    ): SyncManager

    @Binds
    abstract fun bindLogFoodCoordinator(
        impl: LogFoodCoordinatorImpl
    ): LogFoodCoordinator


}