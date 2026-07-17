package com.example.responsiveapp.sync

import com.example.responsiveapp.domain.repository.CustomFoodRepository
import com.example.responsiveapp.domain.repository.DailySummaryRepository
import com.example.responsiveapp.domain.repository.FoodLogRepository
import com.example.responsiveapp.domain.repository.MacroTargetRepository
import com.example.responsiveapp.domain.repository.MyMealRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncManagerImpl @Inject constructor(
    private val foodLogRepository: FoodLogRepository,
    private val customFoodRepository: CustomFoodRepository,
    private val myMealRepository: MyMealRepository,
    private val macroTargetRepository: MacroTargetRepository,
    private val dailySummaryRepository: DailySummaryRepository,
) : SyncManager {

    override suspend fun sync(syncType: SyncType) {
        when (syncType) {
            SyncType.FOOD_LOG -> foodLogRepository.syncPending()
            SyncType.CUSTOM_FOOD -> customFoodRepository.syncPending()
            SyncType.MY_MEAL -> myMealRepository.syncPending()
            SyncType.MACRO_TARGET -> macroTargetRepository.syncPending()
            SyncType.DAILY_SUMMARY -> dailySummaryRepository.syncPending()
            else -> Unit
        }
    }
}
