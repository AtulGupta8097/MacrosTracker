package com.macrivo.app.domain.use_case.food

import com.macrivo.app.core.utils.Resource
import com.macrivo.app.domain.model.FoodItem
import com.macrivo.app.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchFoodsUseCase @Inject constructor(
    private val repository: FoodRepository,
) {

    operator fun invoke(
        query: String,
        limit: Int,
    ): Flow<Resource<List<FoodItem>>> {
        val normalizedInput = query.trim()
        val isDiscoveryFeed = normalizedInput.isBlank()
        val searchExpression = if (isDiscoveryFeed) {
            DISCOVERY_SEARCH_TERMS.random()
        } else {
            normalizedInput
        }

        val results = repository.searchFoods(
            query = searchExpression,
            limit = limit,
        )

        return if (isDiscoveryFeed) {
            results.map { result ->
                if (result is Resource.Success) {
                    Resource.Success(result.data.orEmpty().shuffled())
                } else {
                    result
                }
            }
        } else {
            results
        }
    }

    private companion object {
        val DISCOVERY_SEARCH_TERMS = listOf(
            "chicken",
            "apple",
            "rice",
            "salmon",
            "oatmeal",
            "banana",
            "egg",
            "yogurt",
            "potato",
            "avocado",
            "beans",
            "tofu",
        )
    }
}
