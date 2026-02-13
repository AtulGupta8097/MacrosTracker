package com.example.responsiveapp.data.remote.dto.fatsecret.search

import com.google.gson.annotations.SerializedName

data class FoodSearchResponseDto(
    @SerializedName("foods")
    val foods: FoodsContainerDto?
)