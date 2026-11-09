package com.macrivo.app.domain.use_case.food

import com.macrivo.app.core.utils.Resource
import com.macrivo.app.domain.model.FoodDetail
import com.macrivo.app.domain.repository.FoodRepository
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