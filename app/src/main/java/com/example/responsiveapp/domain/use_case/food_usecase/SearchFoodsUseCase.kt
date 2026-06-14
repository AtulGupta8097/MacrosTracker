package com.example.responsiveapp.domain.use_case.food_usecase

import com.example.responsiveapp.core.utils.Resource
import com.example.responsiveapp.domain.model.FoodItem
import com.example.responsiveapp.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchFoodsUseCase @Inject constructor(
    private val repository: FoodRepository,
) {

    operator fun invoke(
        query: String,
        limit: Int,
    ): Flow<Resource<List<FoodItem>>> {

        val finalQuery = query.ifBlank {
            "Pizza"
        }

        return repository.searchFoods(
            query = finalQuery,
            limit = limit,
        )
    }
}