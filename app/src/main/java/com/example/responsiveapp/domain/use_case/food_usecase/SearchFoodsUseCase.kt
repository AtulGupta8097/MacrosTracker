package com.example.responsiveapp.domain.use_case.food_usecase

import com.example.responsiveapp.domain.model.Food
import com.example.responsiveapp.domain.repository.FoodRepository
import javax.inject.Inject

class SearchFoodsUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(query: String,limit: Int): Result<List<Food>> {

        if (query.isBlank() || query.length < 2) {
            return Result.failure(
                IllegalArgumentException("Search query must be at least 2 characters")
            )
        }
        
        return foodRepository.searchFoods(
            query = query.trim(),
            limit = limit
        )
    }
}