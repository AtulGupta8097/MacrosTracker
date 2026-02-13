package com.example.responsiveapp.data.remote.dto.fatsecret.search

import com.google.gson.annotations.SerializedName

data class FoodItemDto(
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