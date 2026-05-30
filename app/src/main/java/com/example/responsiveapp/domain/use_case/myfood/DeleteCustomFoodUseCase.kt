package com.example.responsiveapp.domain.use_case.myfood

import com.example.responsiveapp.domain.repository.CustomFoodRepository
import javax.inject.Inject

class DeleteCustomFoodUseCase @Inject constructor(
    private val repository: CustomFoodRepository,
) {
    suspend operator fun invoke(foodId: String) = repository.delete(foodId)
}