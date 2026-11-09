package com.macrivo.app.core.di

import com.macrivo.app.data.coordinatior.LogFoodCoordinator
import com.macrivo.app.data.coordinatior.LogFoodCoordinatorImpl
import com.macrivo.app.sync.SyncManager
import com.macrivo.app.sync.SyncManagerImpl
import com.macrivo.app.sync.SyncScheduler
import com.macrivo.app.sync.SyncSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
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