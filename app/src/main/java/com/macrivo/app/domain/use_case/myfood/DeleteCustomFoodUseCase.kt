package com.macrivo.app.domain.use_case.myfood

import com.macrivo.app.domain.repository.CustomFoodRepository
import javax.inject.Inject

class DeleteCustomFoodUseCase @Inject constructor(
    private val repository: CustomFoodRepository,
) {
    suspend operator fun invoke(foodId: String) = repository.delete(foodId)
}