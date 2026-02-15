package com.example.responsiveapp.core.di

import com.example.responsiveapp.BuildConfig
import com.example.responsiveapp.core.constant.Constants.API_BASE_URL
import com.example.responsiveapp.core.constant.Constants.OAUTH_BASE_URL
import com.example.responsiveapp.core.constant.Constants.TIMEOUT_SECONDS
import com.example.responsiveapp.data.datastore.TokenDataStore
import com.example.responsiveapp.data.remote.api.FatSecretApiService
import com.example.responsiveapp.data.remote.api.FatSecretAuthApiService
import com.example.responsiveapp.data.remote.auth.BasicAuthInterceptor
import com.example.responsiveapp.data.remote.auth.FatSecretAuthInterceptor
import com.example.responsiveapp.data.remote.auth.FatSecretAuthenticator
import com.example.responsiveapp.data.remote.auth.FatSecretTokenManager
import com.example.responsiveapp.data.remote.auth.TokenManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideBasicAuthInterceptor(): BasicAuthInterceptor {
        val clientId = BuildConfig.FATSECRET_CLIENT_ID
        val clientSecret = BuildConfig.FATSECRET_CLIENT_SECRET
        return BasicAuthInterceptor(clientId, clientSecret)
    }


    @Provides
    @Singleton
    @AuthOkHttpClient
    fun provideAuthOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        basicAuthInterceptor: BasicAuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(basicAuthInterceptor)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @AuthRetrofit
    fun provideAuthRetrofit(
        @AuthOkHttpClient okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(OAUTH_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(
        @AuthRetrofit retrofit: Retrofit
    ): FatSecretAuthApiService {
        return retrofit.create(FatSecretAuthApiService::class.java)
    }

    // ============ TOKEN MANAGER ============

    @Provides
    @Singleton
    fun provideTokenManager(
        tokenDataStore: TokenDataStore,
        authApiService: FatSecretAuthApiService
    ): TokenManager {
        return FatSecretTokenManager(tokenDataStore, authApiService)
    }

    // ============ MAIN FATSECRET RETROFIT (For API calls) ============

    @Provides
    @Singleton
    fun provideFatSecretAuthInterceptor(
        tokenManager: TokenManager
    ): FatSecretAuthInterceptor {
        return FatSecretAuthInterceptor(tokenManager)
    }

    @Provides
    @Singleton
    fun provideFatSecretAuthenticator(
        tokenManager: TokenManager
    ): FatSecretAuthenticator {
        return FatSecretAuthenticator(tokenManager)
    }

    @Provides
    @Singleton
    @FatSecretOkHttpClient
    fun provideFatSecretOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        fatSecretAuthInterceptor: FatSecretAuthInterceptor,
        fatSecretAuthenticator: FatSecretAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(fatSecretAuthInterceptor)  // Bearer token auth
            .authenticator(fatSecretAuthenticator)     // Auto token refresh
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @FatSecretRetrofit
    fun provideFatSecretRetrofit(
        @FatSecretOkHttpClient okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideFatSecretApiService(
        @FatSecretRetrofit retrofit: Retrofit
    ): FatSecretApiService {
        return retrofit.create(FatSecretApiService::class.java)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FatSecretRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FatSecretOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthOkHttpClient