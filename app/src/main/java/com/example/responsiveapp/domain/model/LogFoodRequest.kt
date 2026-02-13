package com.example.responsiveapp.domain.model

data class LogFoodRequest(
    val userId: String,
    val foodId: String,
    val servingId: String,
    val quantity: Float,
    val date: Long,
    val logMethod: LogMethod,
    val notes: String? = null,
    val imageUrl: String? = null
)
