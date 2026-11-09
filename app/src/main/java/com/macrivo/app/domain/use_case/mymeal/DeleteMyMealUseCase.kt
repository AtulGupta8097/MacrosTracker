package com.macrivo.app.domain.use_case.mymeal

import com.macrivo.app.domain.repository.MyMealRepository
import javax.inject.Inject

class DeleteMyMealUseCase @Inject constructor(
    private val myMealRepository: MyMealRepository
) {
    suspend operator fun invoke(id: String) {
        myMealRepository.deleteMeal(id)
    }
}
