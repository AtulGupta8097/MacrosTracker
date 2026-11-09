package com.macrivo.app.domain.use_case.mymeal

import com.macrivo.app.domain.model.mymeals.MyMeal
import com.macrivo.app.domain.repository.MyMealRepository
import javax.inject.Inject

class SaveMyMealUseCase @Inject constructor(
    private val myMealRepository: MyMealRepository
) {
    suspend operator fun invoke(myMeal: MyMeal) {
        myMealRepository.saveMeal(myMeal)
    }
}