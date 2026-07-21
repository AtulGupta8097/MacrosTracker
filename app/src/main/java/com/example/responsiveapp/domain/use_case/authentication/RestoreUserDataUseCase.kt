package com.example.responsiveapp.domain.use_case.authentication

import com.example.responsiveapp.domain.repository.CustomFoodRepository
import com.example.responsiveapp.domain.repository.DailySummaryRepository
import com.example.responsiveapp.domain.repository.FoodLogRepository
import com.example.responsiveapp.domain.repository.MacroTargetRepository
import com.example.responsiveapp.domain.repository.MyMealRepository
import com.example.responsiveapp.domain.repository.UserProfileRepository
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
    private val myMealRepository: MyMealRepository
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
    }
}
