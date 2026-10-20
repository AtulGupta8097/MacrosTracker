package com.example.responsiveapp.domain.use_case.food

import com.example.responsiveapp.domain.repository.SavedFoodRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveIsFoodSavedUseCase @Inject constructor(
    private val savedFoodRepository: SavedFoodRepository
) {
    operator fun invoke(foodId: String): Flow<Boolean> =
        savedFoodRepository.observeIsSaved(foodId)
}
