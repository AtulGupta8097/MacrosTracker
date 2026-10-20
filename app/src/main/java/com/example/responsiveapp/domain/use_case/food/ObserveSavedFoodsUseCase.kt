package com.example.responsiveapp.domain.use_case.food

import com.example.responsiveapp.domain.model.FoodDetail
import com.example.responsiveapp.domain.repository.SavedFoodRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSavedFoodsUseCase @Inject constructor(
    private val savedFoodRepository: SavedFoodRepository
) {
    operator fun invoke(): Flow<List<FoodDetail>> =
        savedFoodRepository.observeAll()
}
