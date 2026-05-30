package com.example.responsiveapp.domain.use_case.myfood

import com.example.responsiveapp.domain.model.myfood.CustomFood
import com.example.responsiveapp.domain.repository.CustomFoodRepository
import javax.inject.Inject

class SaveCustomFoodUseCase @Inject constructor(
    private val repository: CustomFoodRepository,
) {
    suspend operator fun invoke(food: CustomFood) = repository.save(food)
}