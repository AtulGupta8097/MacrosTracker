package com.example.responsiveapp.data.remote.dto.fatsecret.detail

import com.google.gson.annotations.SerializedName

data class FoodDetailResponse(
    @SerializedName("food")
    val food: FoodDetailDto
)

data class FoodDetailDto(
    @SerializedName("food_id")
    val foodId: String,
    @SerializedName("food_name")
    val foodName: String,
    @SerializedName("food_type")
    val foodType: String,
    @SerializedName("brand_name")
    val brandName: String?,
    @SerializedName("servings")
    val servings: ServingsContainer
)

data class ServingsContainer(
    @SerializedName("serving")
    val serving: List<ServingDto>
)

data class ServingDto(
    @SerializedName("serving_id")
    val servingId: String,
    @SerializedName("serving_description")
    val servingDescription: String,
    @SerializedName("metric_serving_amount")
    val metricServingAmount: String?,
    @SerializedName("metric_serving_unit")
    val metricServingUnit: String?,
    @SerializedName("number_of_units")
    val numberOfUnits: String?,
    @SerializedName("measurement_description")
    val measurementDescription: String?,
    @SerializedName("calories")
    val calories: String?,
    @SerializedName("carbohydrate")
    val carbohydrate: String?,
    @SerializedName("protein")
    val protein: String?,
    @SerializedName("fat")
    val fat: String?,
    @SerializedName("saturated_fat")
    val saturatedFat: String?,
    @SerializedName("polyunsaturated_fat")
    val polyunsaturatedFat: String?,
    @SerializedName("monounsaturated_fat")
    val monounsaturatedFat: String?,
    @SerializedName("cholesterol")
    val cholesterol: String?,
    @SerializedName("sodium")
    val sodium: String?,
    @SerializedName("potassium")
    val potassium: String?,
    @SerializedName("fiber")
    val fiber: String?,
    @SerializedName("sugar")
    val sugar: String?
)