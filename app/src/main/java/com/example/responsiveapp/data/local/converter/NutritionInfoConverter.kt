package com.example.responsiveapp.data.local.converter

import androidx.room.TypeConverter
import com.example.responsiveapp.domain.model.NutritionInfo
import com.google.gson.Gson

class NutritionInfoConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromNutritionInfo(nutritionInfo: NutritionInfo): String {
        return gson.toJson(nutritionInfo)
    }

    @TypeConverter
    fun toNutritionInfo(nutritionJson: String): NutritionInfo {
        return gson.fromJson(
            nutritionJson,
            NutritionInfo::class.java
        )
    }
}