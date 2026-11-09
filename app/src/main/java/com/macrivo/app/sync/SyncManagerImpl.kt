package com.macrivo.app.sync

import com.macrivo.app.domain.repository.CustomFoodRepository
import com.macrivo.app.domain.repository.DailySummaryRepository
import com.macrivo.app.domain.repository.FoodLogRepository
import com.macrivo.app.domain.repository.MacroTargetRepository
import com.macrivo.app.domain.repository.MyMealRepository
import com.macrivo.app.domain.repository.SavedFoodRepository
import com.macrivo.app.domain.repository.UserProfileRepository
import com.macrivo.app.domain.repository.WeightRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncManagerImpl @Inject constructor(
    private val foodLogRepository: FoodLogRepository,
    private val customFoodRepository: CustomFoodRepository,
    private val myMealRepository: MyMealRepository,
    private val macroTargetRepository: MacroTargetRepository,
    private val dailySummaryRepository: DailySummaryRepository,
    private val userProfileRepository: UserProfileRepository,
    private val weightRepository: WeightRepository,
    private val savedFoodRepository: SavedFoodRepository,

    ) : SyncManager {

    override suspend fun sync(syncType: SyncType) {
        when (syncType) {
            SyncType.FOOD_LOG -> foodLogRepository.syncPending()
            SyncType.CUSTOM_FOOD -> customFoodRepository.syncPending()
            SyncType.MY_MEAL -> myMealRepository.syncPending()
            SyncType.MACRO_TARGET -> macroTargetRepository.syncPending()
            SyncType.DAILY_SUMMARY -> dailySummaryRepository.syncPending()
            SyncType.USER_PROFILE -> userProfileRepository.syncPending()
            SyncType.WEIGHT_LOG -> weightRepository.syncPending()
            SyncType.SAVED_FOOD -> savedFoodRepository.syncPending()

        }
    }
}
