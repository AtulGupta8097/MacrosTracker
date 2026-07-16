package com.example.responsiveapp.data.mapper

import com.example.responsiveapp.data.local.converter.IngredientConverters
import com.example.responsiveapp.data.local.entity.MyMealsEntity
import com.example.responsiveapp.data.remote.dto.firebase.MyMealDto
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.model.mymeals.MyMeal

fun MyMeal.toEntity() = MyMealsEntity(
    id = id,
    name = name,
    ingredientsJson = IngredientConverters.fromIngredients(ingredients),
    calories = totalNutritionInfo.calories,
    protein = totalNutritionInfo.protein,
    carbs = totalNutritionInfo.carbs,
    fat = totalNutritionInfo.fat,
    fiber = totalNutritionInfo.fiber,
    sugar = totalNutritionInfo.sugar,
    sodium = totalNutritionInfo.sodium,
    cholesterol = totalNutritionInfo.cholesterol,
    saturatedFat = totalNutritionInfo.saturatedFat,
    polyunsaturatedFat = totalNutritionInfo.polyunsaturatedFat,
    monounsaturatedFat = totalNutritionInfo.monounsaturatedFat,
    transFat = totalNutritionInfo.transFat,
    potassium = totalNutritionInfo.potassium,
    addedSugars = totalNutritionInfo.addedSugars,
    vitaminA = totalNutritionInfo.vitaminA,
    vitaminC = totalNutritionInfo.vitaminC,
    vitaminD = totalNutritionInfo.vitaminD,
    calcium = totalNutritionInfo.calories,
    iron = totalNutritionInfo.iron,
    createdAt = createAt,
    updatedAt = createAt
)

fun MyMealsEntity.toDomain() = MyMeal(
    id = id,
    name = name,
    ingredients = IngredientConverters.toIngredients(ingredientsJson),
    totalNutritionInfo = NutritionInfo(
        calories = calories,
        protein = protein,
        carbs = carbs,
        fat = fat,
        fiber = fiber,
        sugar = sugar,
        sodium = sodium,
        cholesterol = cholesterol,
        saturatedFat = saturatedFat,
        polyunsaturatedFat = polyunsaturatedFat,
        monounsaturatedFat = monounsaturatedFat,
        transFat = transFat,
        potassium = potassium,
        addedSugars = addedSugars,
        vitaminA = vitaminA,
        vitaminC = vitaminC,
        vitaminD = vitaminD,
        calcium = calcium,
        iron = iron
    ),
    createAt = createdAt,
    updatedAt = updatedAt
)

fun MyMealsEntity.toFirestoreDto() =
    MyMealDto(
        id = id,
        name = name,
        ingredientsJson = ingredientsJson,
        calories = calories,
        protein = protein,
        carbs = carbs,
        fat = fat,
        fiber = fiber,
        sugar = sugar,
        sodium = sodium,
        cholesterol = cholesterol,
        saturatedFat = saturatedFat,
        polyunsaturatedFat = polyunsaturatedFat,
        monounsaturatedFat = monounsaturatedFat,
        transFat = transFat,
        potassium = potassium,
        addedSugars = addedSugars,
        vitaminA = vitaminA,
        vitaminC = vitaminC,
        vitaminD = vitaminD,
        calcium = calcium,
        iron = iron,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun List<MyMealsEntity>.toDomain(): List<MyMeal>  {
    return map{
        it.toDomain()
    }
}