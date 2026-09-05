package com.example.responsiveapp.data.remote.dto.fatsecret.barcode

import com.google.gson.annotations.SerializedName

data class BarcodeResponseDto(
    @SerializedName("food_id")
    val foodId: BarcodeFoodIdDto
)

data class BarcodeFoodIdDto(
    @SerializedName("value")
    val value: String
)