package com.example.responsiveapp.domain.repository

import com.example.responsiveapp.domain.model.mymeals.MyMeal
import kotlinx.coroutines.flow.Flow


interface MyMealRepository {

    suspend fun saveMeal(meal: MyMeal)

    suspend fun deleteMeal(id: String)

    fun observeMeals(): Flow<List<MyMeal>>

    suspend fun syncPending()

    suspend fun fetchAndCacheAll()
}