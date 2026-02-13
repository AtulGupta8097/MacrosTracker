package com.example.responsiveapp.domain.use_case.food_usecase

import com.example.responsiveapp.domain.model.Food
import com.example.responsiveapp.domain.repository.FoodRepository
import javax.inject.Inject

class GetFoodByIdUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(foodId: String): Result<Food> {
        require(foodId.isNotBlank()) { "Food ID cannot be blank" }
        return foodRepository.getFoodById(foodId)
    }
}