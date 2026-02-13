package com.example.responsiveapp.core.di

import com.example.responsiveapp.data.datasource.UserProfileLocalDataSource
import com.example.responsiveapp.data.datasource.UserProfileRemoteDataSource
import com.example.responsiveapp.data.repository.AuthRepositoryImp
import com.example.responsiveapp.data.repository.UserProfileRepositoryImpl
import com.example.responsiveapp.domain.repository.AuthRepository
import com.example.responsiveapp.domain.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImp(auth)
    }

    @Singleton
    @Provides
    fun provideFireStore() : FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    fun provideUserProfileRepository(
        local: UserProfileLocalDataSource,
        remote: UserProfileRemoteDataSource
    ): UserProfileRepository {
        return UserProfileRepositoryImpl(
            local = local,
            remote = remote
        )
    }


}