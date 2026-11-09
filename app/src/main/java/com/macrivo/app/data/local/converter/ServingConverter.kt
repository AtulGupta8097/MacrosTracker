package com.macrivo.app.data.local.converter

import androidx.room.TypeConverter
import com.macrivo.app.data.local.entity.ServingEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ServingConverter {

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
