package com.example.responsiveapp.data.remote.api

import com.example.responsiveapp.data.remote.dto.fatsecret.detail.FoodDetailResponseDto
import com.example.responsiveapp.data.remote.dto.fatsecret.search.FoodSearchResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FatSecretApiService {

    @GET("rest/server.api")
    suspend fun searchFoods(
        @Query("search_expression") searchExpression: String,
        @Query("method") method: String = "foods.search",
        @Query("page_number") pageNumber: Int = 0,
        @Query("max_results") maxResults: Int = 50,
        @Query("format") format: String = "json"
    ): Response<FoodSearchResponseDto>

    @GET("rest/server.api")
    suspend fun getFoodById(
        @Query("food_id") foodId: String,
        @Query("method") method: String = "food.get.v2",
        @Query("format") format: String = "json"
    ): Response<FoodDetailResponseDto>
}