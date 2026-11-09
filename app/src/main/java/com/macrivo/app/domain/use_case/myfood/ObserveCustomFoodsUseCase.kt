package com.macrivo.app.domain.use_case.myfood

import com.macrivo.app.domain.model.myfood.CustomFood
import com.macrivo.app.domain.repository.CustomFoodRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCustomFoodsUseCase @Inject constructor(
    private val repository: CustomFoodRepository,
) {
    operator fun invoke(): Flow<List<CustomFood>> = repository.observeAll()
}
