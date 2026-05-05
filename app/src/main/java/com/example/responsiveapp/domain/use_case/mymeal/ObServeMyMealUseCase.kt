package com.example.responsiveapp.domain.use_case.mymeal

import com.example.responsiveapp.domain.model.mymeals.MyMeal
import com.example.responsiveapp.domain.repository.MyMealRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObServeMyMealUseCase @Inject constructor(
    private val myMealRepository: MyMealRepository
) {
    operator fun invoke(): Flow<List<MyMeal>> {
        return myMealRepository.observeMeals()
    }
}