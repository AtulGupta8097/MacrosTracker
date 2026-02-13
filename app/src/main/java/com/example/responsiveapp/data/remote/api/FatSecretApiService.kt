package com.example.responsiveapp.data.remote.api

import com.example.responsiveapp.data.remote.dto.fatsecret.auth.OAuthTokenResponseDto
import com.example.responsiveapp.data.remote.dto.fatsecret.detail.FoodDetailResponseDto
import com.example.responsiveapp.data.remote.dto.fatsecret.search.FoodSearchResponseDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FatSecretApiService {


    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("scope") scope: String = "basic"
    ): Response<OAuthTokenResponseDto>

    @GET("rest/server.api")
    suspend fun searchFoods(
        @Query("method") method: String = "foods.search",
        @Query("search_expression") searchExpression: String,
        @Query("page_number") pageNumber: Int = 0,
        @Query("max_results") maxResults: Int = 50,
        @Query("format") format: String = "json"
    ): Response<FoodSearchResponseDto>

    @GET("rest/server.api")
    suspend fun getFoodById(
        @Query("method") method: String = "food.get.v2",
        @Query("food_id") foodId: String,
        @Query("format") format: String = "json"
    ): Response<FoodDetailResponseDto>
}