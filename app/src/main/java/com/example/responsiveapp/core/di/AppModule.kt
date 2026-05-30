package com.example.responsiveapp.core.di

import android.content.Context
import com.example.responsiveapp.data.datasource.MacroCalculatorDataSource
import com.example.responsiveapp.data.datasource.MacroCalculatorDataSourceImpl
import com.example.responsiveapp.data.datasource.UserProfileLocalDataSource
import com.example.responsiveapp.data.datasource.UserProfileRemoteDataSource
import com.example.responsiveapp.data.datastore.EncryptedTokenDataStore
import com.example.responsiveapp.data.datastore.TokenDataStore
import com.example.responsiveapp.data.local.dao.CustomFoodDao
import com.example.responsiveapp.data.local.dao.FoodDetailDao
import com.example.responsiveapp.data.local.dao.FoodItemDao
import com.example.responsiveapp.data.local.dao.FoodLogDao
import com.example.responsiveapp.data.local.dao.MyMealsDao
import com.example.responsiveapp.data.remote.api.FatSecretApiService
import com.example.responsiveapp.data.repository.AuthRepositoryImp
import com.example.responsiveapp.data.repository.CustomFoodRepositoryImpl
import com.example.responsiveapp.data.repository.FoodLogRepositoryImpl
import com.example.responsiveapp.data.repository.FoodRepositoryImpl
import com.example.responsiveapp.data.repository.MacroRepositoryImpl
import com.example.responsiveapp.data.repository.MyMealRepositoryImpl
import com.example.responsiveapp.data.repository.UserProfileRepositoryImpl
import com.example.responsiveapp.domain.repository.AuthRepository
import com.example.responsiveapp.domain.repository.CustomFoodRepository
import com.example.responsiveapp.domain.repository.FoodLogRepository
import com.example.responsiveapp.domain.repository.FoodRepository
import com.example.responsiveapp.domain.repository.MacroRepository
import com.example.responsiveapp.domain.repository.MyMealRepository
import com.example.responsiveapp.domain.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth,
    ): AuthRepository =
        AuthRepositoryImp(auth)

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        val firestore = FirebaseFirestore.getInstance()

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(
                FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
            )
            .build()

        firestore.firestoreSettings = settings

        return firestore
    }

    @Provides
    fun provideMacroCalculatorDataSource():
            MacroCalculatorDataSource =
        MacroCalculatorDataSourceImpl()

    @Provides
    fun provideMacroRepository(
        dataSource: MacroCalculatorDataSource,
    ): MacroRepository =
        MacroRepositoryImpl(dataSource)

    @Provides
    fun provideUserProfileRepository(
        local: UserProfileLocalDataSource,
        remote: UserProfileRemoteDataSource,
    ): UserProfileRepository =
        UserProfileRepositoryImpl(
            local = local,
            remote = remote
        )

    @Provides
    @Singleton
    fun provideTokenDataStore(
        @ApplicationContext context: Context,
    ): TokenDataStore =
        EncryptedTokenDataStore(context)

    @Provides
    @Singleton
    fun provideFoodRepository(
        foodDao: FoodItemDao,
        foodDetailDao: FoodDetailDao,
        fatSecretApi: FatSecretApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): FoodRepository =
        FoodRepositoryImpl(
            foodDao,
            foodDetailDao,
            fatSecretApi,
            ioDispatcher
        )

    @Provides
    @Singleton
    fun provideFoodLogRepository(
        foodLogDao: FoodLogDao,
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
    ): FoodLogRepository =
        FoodLogRepositoryImpl(
            foodLogDao,
            firestore,
            firebaseAuth
        )

    @Singleton
    @Provides
    fun provideMyMealRepository(
        myMealDao: MyMealsDao,
    ): MyMealRepository =
        MyMealRepositoryImpl(myMealDao)

    @Singleton
    @Provides
    fun provideCustomFoodRepository(
        dao: CustomFoodDao,
    ): CustomFoodRepository =
        CustomFoodRepositoryImpl(dao)

    @IoDispatcher
    @Provides
    fun providesIoDispatcher():
            CoroutineDispatcher =
        Dispatchers.IO
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher