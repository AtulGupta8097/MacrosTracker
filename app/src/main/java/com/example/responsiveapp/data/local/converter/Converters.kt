package com.example.responsiveapp.data.local.converter

import androidx.room.TypeConverter
import com.example.responsiveapp.data.local.entity.ServingEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    
    private val gson = Gson()
    
    @TypeConverter
    fun fromServingList(servings: List<ServingEntity>): String {
        return gson.toJson(servings)
    }
    
    @TypeConverter
    fun toServingList(servingsJson: String): List<ServingEntity> {
        val type = object : TypeToken<List<ServingEntity>>() {}.type
        return gson.fromJson(servingsJson, type)
    }
}