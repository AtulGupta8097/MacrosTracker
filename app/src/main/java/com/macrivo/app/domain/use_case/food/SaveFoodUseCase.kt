package com.macrivo.app.domain.use_case.food

import com.macrivo.app.domain.model.FoodDetail
import com.macrivo.app.domain.repository.SavedFoodRepository
import javax.inject.Inject

class SaveFoodUseCase @Inject constructor(
    private val savedFoodRepository: SavedFoodRepository
) {
    suspend operator fun invoke(food: FoodDetail) {
        savedFoodRepository.save(food)
    }
}
