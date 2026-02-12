package com.example.responsiveapp.data.remote.dto

data class UserProfileDto(
    val gender: String? = null,
    val age: Int? = null,
    val weight: Float? = null,
    val height: Float? = null,
    val activityLevel: String? = null,
    val goal: String? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null
)