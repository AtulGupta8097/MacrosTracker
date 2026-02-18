package com.example.responsiveapp.domain.use_case.food_usecase

import com.example.responsiveapp.core.utils.Resource
import com.example.responsiveapp.domain.model.Food
import com.example.responsiveapp.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFoodByIdUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {
    operator fun invoke(foodId: String): Flow<Resource<Food>> = flow{
        emit(Resource.Loading())
        val result = foodRepository.getFoodById(foodId)
        result.fold(
            onSuccess = { food ->
                emit(Resource.Success(food))
            },
            onFailure = { e ->
                emit(Resource.Error(e.message ?: "Unknown error"))
            }
        )

    }
}