package com.macrivo.app.domain.use_case.mymeal

import com.macrivo.app.domain.model.mymeals.MyMeal
import com.macrivo.app.domain.repository.MyMealRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObServeMyMealUseCase @Inject constructor(
    private val myMealRepository: MyMealRepository
) {
    operator fun invoke(): Flow<List<MyMeal>> {
        return myMealRepository.observeMeals()
    }
}