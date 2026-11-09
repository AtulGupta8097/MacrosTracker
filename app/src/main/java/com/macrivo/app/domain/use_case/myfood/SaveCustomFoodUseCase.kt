package com.macrivo.app.domain.use_case.myfood

import com.macrivo.app.domain.model.myfood.CustomFood
import com.macrivo.app.domain.repository.CustomFoodRepository
import javax.inject.Inject

class SaveCustomFoodUseCase @Inject constructor(
    private val repository: CustomFoodRepository,
) {
    suspend operator fun invoke(food: CustomFood) = repository.save(food)
}