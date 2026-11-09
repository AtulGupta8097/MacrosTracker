package com.macrivo.app.domain.use_case.authentication

import com.macrivo.app.domain.repository.CustomFoodRepository
import com.macrivo.app.domain.repository.DailySummaryRepository
import com.macrivo.app.domain.repository.FoodLogRepository
import com.macrivo.app.domain.repository.MacroTargetRepository
import com.macrivo.app.domain.repository.MyMealRepository
import com.macrivo.app.domain.repository.SavedFoodRepository
import com.macrivo.app.domain.repository.UserProfileRepository
import com.macrivo.app.domain.repository.WeightRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RestoreUserDataUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val macroTargetRepository: MacroTargetRepository,
    private val foodLogRepository: FoodLogRepository,
    private val dailySummaryRepository: DailySummaryRepository,
    private val customFoodRepository: CustomFoodRepository,
    private val myMealRepository: MyMealRepository,
    private val weightRepository: WeightRepository,
    private val savedFoodRepository: SavedFoodRepository,
) {
    suspend operator fun invoke() = coroutineScope {
        
        // UserProfileRepository doesn't catch internally like the others,
        launch {
            runCatching { userProfileRepository.getUserProfile() }
        }
        launch { macroTargetRepository.fetchAndCacheAll() }
        launch { foodLogRepository.fetchAndCacheAll() }
        launch { dailySummaryRepository.fetchAndCacheAll() }
        launch { customFoodRepository.fetchAndCacheAll() }
        launch { myMealRepository.fetchAndCacheAll() }
        launch { weightRepository.fetchAndCacheAll() }
        launch { savedFoodRepository.fetchAndCacheAll() }
    }
}
