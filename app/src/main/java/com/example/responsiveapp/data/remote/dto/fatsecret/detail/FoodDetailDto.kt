package com.example.responsiveapp.data.remote.dto.fatsecret.detail

import com.google.gson.annotations.SerializedName

data class FoodDetailDto(
    @SerializedName("food_id")
    val foodId: String,
    @SerializedName("food_name")
    val foodName: String,
    @SerializedName("food_type")
    val foodType: String,
    @SerializedName("brand_name")
    val brandName: String?,
    @SerializedName("servings")
    val servings: ServingsContainerDto
)