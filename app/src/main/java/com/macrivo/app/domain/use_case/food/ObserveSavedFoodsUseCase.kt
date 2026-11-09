package com.macrivo.app.domain.use_case.food

import com.macrivo.app.domain.model.FoodDetail
import com.macrivo.app.domain.repository.SavedFoodRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSavedFoodsUseCase @Inject constructor(
    private val savedFoodRepository: SavedFoodRepository
) {
    operator fun invoke(): Flow<List<FoodDetail>> =
        savedFoodRepository.observeAll()
}
