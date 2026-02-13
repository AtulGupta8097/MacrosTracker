package com.example.responsiveapp.data.remote.dto.fatsecret.detail

import com.google.gson.annotations.SerializedName

data class FoodDetailResponseDto(
    @SerializedName("food")
    val food: FoodDetailDto
)