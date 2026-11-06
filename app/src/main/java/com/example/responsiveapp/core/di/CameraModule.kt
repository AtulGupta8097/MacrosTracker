package com.example.responsiveapp.core.di

import com.example.responsiveapp.core.camera.CameraHelper
import com.example.responsiveapp.core.camera.CameraHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object CameraModule {

    @Provides
    @ViewModelScoped
    fun provideCameraHelper(): CameraHelper = CameraHelperImpl()
}
