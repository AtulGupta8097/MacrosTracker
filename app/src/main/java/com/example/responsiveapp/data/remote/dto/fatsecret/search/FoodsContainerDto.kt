package com.example.responsiveapp.data.remote.dto.fatsecret.search

import com.google.gson.annotations.SerializedName

data class FoodsContainerDto(
    @SerializedName("food")
    val food: List<FoodItemDto>?,
    @SerializedName("max_results")
    val maxResults: String?,
    @SerializedName("page_number")
    val pageNumber: String?,
    @SerializedName("total_results")
    val totalResults: String?
)