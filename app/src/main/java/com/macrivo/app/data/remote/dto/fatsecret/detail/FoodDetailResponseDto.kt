package com.macrivo.app.data.remote.dto.fatsecret.detail

import com.google.gson.annotations.SerializedName

data class FoodDetailResponseDto(
    @SerializedName("food")
    val food: FoodDetailDto
)