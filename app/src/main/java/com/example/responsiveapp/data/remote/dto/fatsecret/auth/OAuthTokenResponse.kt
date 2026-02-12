package com.example.responsiveapp.data.remote.dto.fatsecret.auth

import com.google.gson.annotations.SerializedName

data class OAuthTokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_in")
    val expiresIn: Long
)