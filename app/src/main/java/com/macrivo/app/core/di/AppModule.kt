package com.macrivo.app.core.di

import android.content.Context
import androidx.work.WorkManager
import com.macrivo.app.data.datastore.EncryptedTokenDataStore
import com.macrivo.app.data.datastore.TokenDataStore
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
import com.macrivo.app.data.remote.api.FatSecretApiService
import com.macrivo.app.data.repository.AuthRepositoryImp
import com.macrivo.app.data.repository.CustomFoodRepositoryImpl
import com.macrivo.app.data.repository.DailySummaryRepositoryImpl
import com.macrivo.app.data.repository.FoodLogRepositoryImpl
import com.macrivo.app.data.repository.FoodRepositoryImpl
import com.macrivo.app.data.repository.MacroTargetRepositoryImpl
import com.macrivo.app.data.repository.MyMealRepositoryImpl
import com.macrivo.app.data.repository.SavedFoodRepositoryImpl
import com.macrivo.app.data.repository.UserProfileRepositoryImpl
import com.macrivo.app.data.repository.WeightRepositoryImpl
import com.macrivo.app.domain.repository.AuthRepository
import com.macrivo.app.domain.repository.CustomFoodRepository
import com.macrivo.app.domain.repository.DailySummaryRepository
import com.macrivo.app.domain.repository.FoodLogRepository
import com.macrivo.app.domain.repository.FoodRepository
import com.macrivo.app.domain.repository.MacroTargetRepository
import com.macrivo.app.domain.repository.MyMealRepository
import com.macrivo.app.domain.repository.SavedFoodRepository
import com.macrivo.app.domain.repository.UserProfileRepository
import com.macrivo.app.domain.repository.WeightRepository
import com.macrivo.app.domain.session.SessionManager
import com.macrivo.app.domain.session.SessionManagerImpl
import com.macrivo.app.sync.SyncScheduler
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
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository =
        AuthRepositoryImp(auth)

    @Singleton
    @Provides
    fun provideSessionManager(auth: FirebaseAuth): SessionManager =
        SessionManagerImpl(auth)

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
    @Singleton
    fun provideMacroTargetRepository(
        dao: MacroTargetDao,
        firestore: FirebaseFirestore,
        sessionManager: SessionManager,
        syncScheduler: SyncScheduler
    ): MacroTargetRepository =
        MacroTargetRepositoryImpl(
            dao,
            firestore,
            sessionManager,
            syncScheduler
        )

    @Provides
    @Singleton
    fun provideUserProfileRepository(
        dao: UserProfileDao,
        firestore: FirebaseFirestore,
        sessionManager: SessionManager,
        syncScheduler: SyncScheduler,
    ): UserProfileRepository =
        UserProfileRepositoryImpl(
            dao = dao,
            firestore = firestore,
            sessionManager = sessionManager,
            scheduler = syncScheduler
        )

    @Provides
    @Singleton
    fun provideTokenDataStore(@ApplicationContext context: Context): TokenDataStore =
        EncryptedTokenDataStore(context)

    @Provides
    @Singleton
    fun provideFoodRepository(
        foodSearchDao: FoodSearchDao,
        foodDetailDao: FoodDetailDao,
        fatSecretApi: FatSecretApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): FoodRepository =
        FoodRepositoryImpl(foodSearchDao, foodDetailDao, fatSecretApi, ioDispatcher)

    @Provides
    @Singleton
    fun provideFoodLogRepository(
        foodLogDao: FoodLogDao,
        firestore: FirebaseFirestore,
        sessionManager: SessionManager,
    ): FoodLogRepository =
        FoodLogRepositoryImpl(foodLogDao, firestore, sessionManager)

    @Provides
    @Singleton
    fun provideDailySummaryRepository(
        dailySummaryDao: DailySummaryDao,
        firestore: FirebaseFirestore,
        sessionManager: SessionManager,
    ): DailySummaryRepository =
        DailySummaryRepositoryImpl(dailySummaryDao, firestore, sessionManager)

    @Singleton
    @Provides
    fun provideMyMealRepository(
        myMealDao: MyMealsDao,
        firestore: FirebaseFirestore,
        sessionManager: SessionManager,
        scheduler: SyncScheduler
    ): MyMealRepository =
        MyMealRepositoryImpl(
            myMealDao,
            firestore,
            sessionManager,
            scheduler
        )

    @Singleton
    @Provides
    fun provideCustomFoodRepository(
        dao: CustomFoodDao,
        firestore: FirebaseFirestore,
        sessionManager: SessionManager,
        scheduler: SyncScheduler,
    ): CustomFoodRepository =
        CustomFoodRepositoryImpl(dao, firestore, sessionManager, scheduler)

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideWorkManager(
        @ApplicationContext context: Context
    ): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideWeightRepository(
        dao: WeightLogDao,
        firestore: FirebaseFirestore,
        sessionManager: SessionManager,
        scheduler: SyncScheduler,
    ): WeightRepository =
        WeightRepositoryImpl(dao, firestore, sessionManager, scheduler)

    @Singleton
    @Provides
    fun provideSavedFoodRepository(
        dao: SavedFoodDao,
        firestore: FirebaseFirestore,
        sessionManager: SessionManager,
        scheduler: SyncScheduler,
    ): SavedFoodRepository =
        SavedFoodRepositoryImpl(dao, firestore, sessionManager, scheduler)

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher