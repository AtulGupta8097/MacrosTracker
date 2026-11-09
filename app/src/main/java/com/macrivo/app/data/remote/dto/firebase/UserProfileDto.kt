package com.macrivo.app.data.remote.dto.firebase

data class UserProfileDto(
    val name: String? = null,
    val gender: String? = null,
    val age: Int? = null,
    val weight: Float? = null,
    val height: Float? = null,
    val targetWeight: Float? = null,
    val activityLevel: String? = null,
    val goal: String? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null
)