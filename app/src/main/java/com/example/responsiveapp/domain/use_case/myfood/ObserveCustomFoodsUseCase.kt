package com.example.responsiveapp.domain.use_case.myfood

import com.example.responsiveapp.domain.model.myfood.CustomFood
import com.example.responsiveapp.domain.repository.CustomFoodRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCustomFoodsUseCase @Inject constructor(
    private val repository: CustomFoodRepository,
) {
    operator fun invoke(): Flow<List<CustomFood>> = repository.observeAll()
}
