package com.example.responsiveapp.data.local.converter

import androidx.room.TypeConverter
import com.example.responsiveapp.domain.model.MealIngredient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object IngredientConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromIngredients(list: List<MealIngredient>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toIngredients(json: String): List<MealIngredient> {
        val type = object : TypeToken<List<MealIngredient>>() {}.type
        return gson.fromJson(json, type)
    }
}