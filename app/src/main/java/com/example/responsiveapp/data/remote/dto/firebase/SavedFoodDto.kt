package com.example.responsiveapp.data.remote.dto.firebase


data class SavedFoodDto(
    val id: String = "",
    val name: String = "",
    val brand: String? = null,
    val foodType: String = "",
    val imageUrl: String? = null,
    val barcode: String? = null,
    val verified: Boolean = true,
    val servingsJson: String = "[]",
    val savedAt: Long = 0L,
)
