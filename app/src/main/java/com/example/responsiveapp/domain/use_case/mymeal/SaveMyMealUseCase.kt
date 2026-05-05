package com.example.responsiveapp.domain.use_case.mymeal

import com.example.responsiveapp.data.local.dao.MyMealsDao
import com.example.responsiveapp.data.mapper.toEntity
import com.example.responsiveapp.domain.model.mymeals.MyMeal
import com.example.responsiveapp.domain.repository.MyMealRepository
import javax.inject.Inject

class SaveMyMealUseCase @Inject constructor(
    private val myMealRepository: MyMealRepository
) {
    suspend operator fun invoke(myMeal: MyMeal) {
        myMealRepository.saveMeal(myMeal)
    }
}