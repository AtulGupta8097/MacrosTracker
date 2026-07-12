package com.example.responsiveapp.core.di

import com.example.responsiveapp.sync.SyncScheduler
import com.example.responsiveapp.sync.SyncSchedulerImpl
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

}