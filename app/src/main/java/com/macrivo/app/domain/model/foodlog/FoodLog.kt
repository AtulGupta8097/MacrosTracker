package com.macrivo.app.domain.model.foodlog

import com.macrivo.app.core.utils.DateUtils
import com.macrivo.app.domain.model.MealIngredient
import com.macrivo.app.domain.model.NutritionInfo
import com.macrivo.app.domain.model.SyncStatus
import com.macrivo.app.domain.model.scale

data class FoodLog(
    val id: String,
    val date: Long = 0L,
    val foodName: String = "",
    val servingDescription: String = "",
    val quantity: Float = 1f,
    val nutrition: NutritionInfo = NutritionInfo(),
    val ingredients: List<MealIngredient> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val retryCount: Int = 0,
    val lastSyncAttempt: Long? = null
)

fun FoodLog.withQuantity(newQuantity: Float, now: Long = System.currentTimeMillis()): FoodLog {
    require(newQuantity > 0f) { "Quantity must be greater than zero." }
    if (quantity <= 0f) return copy(quantity = newQuantity, updatedAt = now)

    val factor = newQuantity / quantity

    return copy(
        quantity = newQuantity,
        nutrition = nutrition.scale(factor),
        ingredients = ingredients.map {
            it.copy(quantity = it.quantity * factor, nutrition = it.nutrition.scale(factor))
        },
        updatedAt = now,
    )
}


val FoodLog.isEditable: Boolean
    get() = DateUtils.isToday(date)
