package com.example.responsiveapp.domain.use_case.mymeal

import com.example.responsiveapp.domain.repository.MyMealRepository
import javax.inject.Inject

class DeleteMyMealUseCase @Inject constructor(
    private val myMealRepository: MyMealRepository
) {
    suspend operator fun invoke(id: String) {
        myMealRepository.deleteMeal(id)
    }
}
