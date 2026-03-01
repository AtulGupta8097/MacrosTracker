package com.example.responsiveapp.data.remote.dto.fatsecret.detail

import com.google.gson.annotations.SerializedName

data class ServingDto(
    @SerializedName("serving_id")
    val servingId: String,
    @SerializedName("serving_description")
    val servingDescription: String,
    @SerializedName("metric_serving_amount")
    val metricServingAmount: Float?,
    @SerializedName("metric_serving_unit")
    val metricServingUnit: String?,
    @SerializedName("number_of_units")
    val numberOfUnits: Float?,
    @SerializedName("measurement_description")
    val measurementDescription: String?,
    @SerializedName("calories")
    val calories: Float?,
    @SerializedName("carbohydrate")
    val carbohydrate: Float?,
    @SerializedName("protein")
    val protein: Float?,
    @SerializedName("fat")
    val fat: Float?,
    @SerializedName("saturated_fat")
    val saturatedFat: Float?,
    @SerializedName("polyunsaturated_fat")
    val polyunsaturatedFat: Float?,
    @SerializedName("monounsaturated_fat")
    val monounsaturatedFat: Float?,
    @SerializedName("trans_fat")
    val transFat: Float?,
    @SerializedName("cholesterol")
    val cholesterol: Float?,
    @SerializedName("sodium")
    val sodium: Float?,
    @SerializedName("potassium")
    val potassium: Float?,
    @SerializedName("fiber")
    val fiber: Float?,
    @SerializedName("sugar")
    val sugar: Float?,
    @SerializedName("added_sugars")
    val addedSugars: Float?,
    @SerializedName("vitamin_a")
    val vitaminA: Float?,
    @SerializedName("vitamin_c")
    val vitaminC: Float?,
    @SerializedName("vitamin_d")
    val vitaminD: Float?,
    @SerializedName("calcium")
    val calcium: Float?,
    @SerializedName("iron")
    val iron: Float?,
)