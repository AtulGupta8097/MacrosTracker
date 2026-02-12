package com.example.responsiveapp.data.remote.api

import com.example.responsiveapp.data.remote.dto.fatsecret.auth.OAuthTokenResponse
import com.example.responsiveapp.data.remote.dto.fatsecret.detail.FoodDetailResponse
import com.example.responsiveapp.data.remote.dto.fatsecret.search.FoodSearchResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FatSecretApiService {
    
    /**
     * OAuth 2.0 Token endpoint
     */
    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("scope") scope: String = "basic"
    ): Response<OAuthTokenResponse>
    
    /**
     * Search foods
     * Method: foods.search
     */
    @GET("rest/server.api")
    suspend fun searchFoods(
        @Query("method") method: String = "foods.search",
        @Query("search_expression") searchExpression: String,
        @Query("page_number") pageNumber: Int = 0,
        @Query("max_results") maxResults: Int = 50,
        @Query("format") format: String = "json"
    ): Response<FoodSearchResponse>
    
    /**
     * Get food details by ID
     * Method: food.get.v2
     */
    @GET("rest/server.api")
    suspend fun getFoodById(
        @Query("method") method: String = "food.get.v2",
        @Query("food_id") foodId: String,
        @Query("format") format: String = "json"
    ): Response<FoodDetailResponse>
    
    /**
     * Get food by barcode
     * Method: food.find_id_for_barcode
     */
    @GET("rest/server.api")
    suspend fun getFoodByBarcode(
        @Query("method") method: String = "food.find_id_for_barcode",
        @Query("barcode") barcode: String,
        @Query("format") format: String = "json"
    ): Response<FoodDetailResponse>
}