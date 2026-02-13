package com.example.responsiveapp.data.remote.dto.firebase

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.security.Timestamp

data class FirebaseFoodLogDto(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val foodId: String = "",
    val foodName: String = "",
    val brand: String? = null,
    val servingId: String = "",
    val servingDescription: String = "",
    val quantity: Float = 1f,
    val calories: Float = 0f,
    val protein: Float = 0f,
    val carbs: Float = 0f,
    val fat: Float = 0f,
    val fiber: Float = 0f,
    val sugar: Float = 0f,
    val sodium: Float = 0f,
    val cholesterol: Float = 0f,
    val saturatedFat: Float = 0f,
    val transFat: Float = 0f,
    val date: Long = 0L,
    val logMethod: String = "DATABASE",
    val notes: String? = null,
    val imageUrl: String? = null,
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val updatedAt: Timestamp? = null,
    val syncStatus: String = "SYNCED"
)