package com.example.responsiveapp.data.remote.api

import com.example.responsiveapp.data.remote.dto.fatsecret.auth.OAuthTokenResponseDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface FatSecretAuthApiService {

    @FormUrlEncoded
    @POST("connect/token")
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("scope") scope: String = "basic"
    ): Response<OAuthTokenResponseDto>
}