package com.example.responsiveapp.domain.use_case.food_usecase

import com.example.responsiveapp.domain.model.Food
import com.example.responsiveapp.domain.repository.FoodRepository
import javax.inject.Inject

class SearchFoodsUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(params: Params): Result<List<Food>> {

        if (params.query.isBlank() || params.query.length < 2) {
            return Result.failure(
                IllegalArgumentException("Search query must be at least 2 characters")
            )
        }
        
        return foodRepository.searchFoods(
            query = params.query.trim(),
            limit = params.limit
        )
    }
    
    data class Params(
        val query: String,
        val limit: Int = 20
    )
}