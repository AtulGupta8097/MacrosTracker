package com.example.responsiveapp.domain.use_case.food

import com.example.responsiveapp.core.utils.Resource
import com.example.responsiveapp.domain.model.FoodDetail
import com.example.responsiveapp.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

const val NO_BARCODE_MATCH_MESSAGE = "no_barcode_match"

class GetFoodDetailByBarcodeUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {
    operator fun invoke(barcode: String): Flow<Resource<FoodDetail>> = flow {
        emit(Resource.Loading())
        val result = foodRepository.getFoodDetailByBarcode(barcode)
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