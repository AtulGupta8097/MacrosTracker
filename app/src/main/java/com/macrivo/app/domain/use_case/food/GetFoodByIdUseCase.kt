package com.macrivo.app.domain.use_case.food

import com.macrivo.app.core.utils.Resource
import com.macrivo.app.domain.model.FoodDetail
import com.macrivo.app.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFoodByIdUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {
    operator fun invoke(foodId: String): Flow<Resource<FoodDetail>> = flow{
        emit(Resource.Loading())
        val result = foodRepository.getFoodDetail(foodId)
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