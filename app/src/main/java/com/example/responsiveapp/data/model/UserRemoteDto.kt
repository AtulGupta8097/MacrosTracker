package com.example.responsiveapp.data.model

// data/remote/model/UserRemoteDto.kt
data class UserRemoteDto(
    val gender: String? = null,
    val age: Int? = null,
    val weight: Double? = null,
    val height: Double? = null,
    val activityLevel: String? = null,
    val goal: String? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null
)
