package com.example.responsiveapp.data.remote.dto.fatsecret.detail

import com.google.gson.annotations.SerializedName

data class ServingsContainerDto(
    @SerializedName("serving")
    val serving: List<ServingDto>
)