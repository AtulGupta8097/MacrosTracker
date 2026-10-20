package com.example.responsiveapp.domain.use_case.food

import com.example.responsiveapp.domain.model.FoodDetail
import com.example.responsiveapp.domain.repository.SavedFoodRepository
import javax.inject.Inject

class SaveFoodUseCase @Inject constructor(
    private val savedFoodRepository: SavedFoodRepository
) {
    suspend operator fun invoke(food: FoodDetail) {
        savedFoodRepository.save(food)
    }
}
