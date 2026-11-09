package com.macrivo.app.data.remote.dto.fatsecret.search

import com.google.gson.annotations.SerializedName

data class FoodSearchResponseDto(
    @SerializedName("foods")
    val foods: FoodsContainerDto?
)