package com.example.responsiveapp.data.repository

import com.example.responsiveapp.data.local.dao.MyMealsDao
import com.example.responsiveapp.data.mapper.toDomain
import com.example.responsiveapp.data.mapper.toEntity
import com.example.responsiveapp.domain.model.mymeals.MyMeal
import com.example.responsiveapp.domain.repository.MyMealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MyMealRepositoryImpl(
    private val myMealDao: MyMealsDao
): MyMealRepository {

    override suspend fun saveMeal(meal: MyMeal) {
        myMealDao.insertMyMeal(meal.toEntity())
    }

    override fun observeMeals(): Flow<List<MyMeal>> {
        return myMealDao.observeMyMeals().map {
            it.toDomain()
        }
    }

    override suspend fun deleteMeal(myMealid: String) {
        myMealDao.deleteMyMealsById(myMealid)
    }

}