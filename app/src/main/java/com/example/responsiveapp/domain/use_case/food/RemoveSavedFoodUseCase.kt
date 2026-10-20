package com.example.responsiveapp.domain.use_case.food

import com.example.responsiveapp.domain.repository.SavedFoodRepository
import javax.inject.Inject

class RemoveSavedFoodUseCase @Inject constructor(
    private val savedFoodRepository: SavedFoodRepository
) {
    suspend operator fun invoke(foodId: String) {
        savedFoodRepository.remove(foodId)
    }
}
