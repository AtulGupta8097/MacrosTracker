package com.example.responsiveapp.core.di

import android.content.Context
import com.example.responsiveapp.data.datasource.MacroCalculatorDataSource
import com.example.responsiveapp.data.datasource.MacroCalculatorDataSourceImpl
import com.example.responsiveapp.data.datasource.UserProfileLocalDataSource
import com.example.responsiveapp.data.datasource.UserProfileRemoteDataSource
import com.example.responsiveapp.data.datastore.EncryptedTokenDataStore
import com.example.responsiveapp.data.datastore.TokenDataStore
import com.example.responsiveapp.data.local.dao.FoodDao
import com.example.responsiveapp.data.local.dao.FoodLogDao
import com.example.responsiveapp.data.remote.api.FatSecretApiService
import com.example.responsiveapp.data.repository.AuthRepositoryImp
import com.example.responsiveapp.data.repository.FoodLogRepositoryImpl
import com.example.responsiveapp.data.repository.FoodRepositoryImpl
import com.example.responsiveapp.data.repository.MacroRepositoryImpl
import com.example.responsiveapp.data.repository.UserProfileRepositoryImpl
import com.example.responsiveapp.domain.repository.AuthRepository
import com.example.responsiveapp.domain.repository.FoodLogRepository
import com.example.responsiveapp.domain.repository.FoodRepository
import com.example.responsiveapp.domain.repository.MacroRepository
import com.example.responsiveapp.domain.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        val firestore = FirebaseFirestore.getInstance()

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()

        firestore.firestoreSettings = settings

        return firestore
    }

    @Provides
    fun provideMacroCalculatorDataSource(): MacroCalculatorDataSource =
        MacroCalculatorDataSourceImpl()

    @Provides
    fun provideMacroRepository(
        dataSource: MacroCalculatorDataSource
    ): MacroRepository =
        MacroRepositoryImpl(dataSource)

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

    @Provides
    @Singleton
    fun provideTokenDataStore(
        @ApplicationContext context: Context
    ): TokenDataStore {
        return EncryptedTokenDataStore(context)
    }

    @Provides
    @Singleton
    fun provideFoodRepository(
        foodDao: FoodDao,
        fatSecretApi: FatSecretApiService
    ): FoodRepository {
        return FoodRepositoryImpl(foodDao, fatSecretApi)

    }

    @Provides
    @Singleton
    fun provideFoodLogRepository(
        foodLogDao: FoodLogDao,
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): FoodLogRepository {
        return FoodLogRepositoryImpl(foodLogDao, firestore, firebaseAuth)
    }
}