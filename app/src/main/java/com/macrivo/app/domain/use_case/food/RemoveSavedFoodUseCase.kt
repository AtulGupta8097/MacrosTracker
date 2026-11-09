package com.macrivo.app.domain.use_case.food

import com.macrivo.app.domain.repository.SavedFoodRepository
import javax.inject.Inject

class RemoveSavedFoodUseCase @Inject constructor(
    private val savedFoodRepository: SavedFoodRepository
) {
    suspend operator fun invoke(foodId: String) {
        savedFoodRepository.remove(foodId)
    }
}
