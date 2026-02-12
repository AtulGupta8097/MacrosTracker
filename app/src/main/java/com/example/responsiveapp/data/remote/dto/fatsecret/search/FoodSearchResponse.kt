package com.example.responsiveapp.data.remote.dto.fatsecret.search

import com.google.gson.annotations.SerializedName

data class FoodSearchResponse(
    @SerializedName("foods")
    val foods: FoodsContainer?
)

data class FoodsContainer(
    @SerializedName("food")
    val food: List<FoodDto>?,
    @SerializedName("max_results")
    val maxResults: String?,
    @SerializedName("page_number")
    val pageNumber: String?,
    @SerializedName("total_results")
    val totalResults: String?
)

data class FoodDto(
    @SerializedName("food_id")
    val foodId: String,
    @SerializedName("food_name")
    val foodName: String,
    @SerializedName("food_type")
    val foodType: String,
    @SerializedName("food_description")
    val foodDescription: String?,
    @SerializedName("brand_name")
    val brandName: String?
)