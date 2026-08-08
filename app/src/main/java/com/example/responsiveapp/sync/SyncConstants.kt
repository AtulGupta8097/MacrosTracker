package com.example.responsiveapp.sync
object SyncConstants {

    const val KEY_SYNC_TYPE = "sync_type"

    fun uniqueWorkNameFor(syncType: SyncType): String = when (syncType) {
        SyncType.FOOD_LOG -> "sync_food_log"
        SyncType.CUSTOM_FOOD -> "sync_custom_food"
        SyncType.MY_MEAL -> "sync_my_meal"
        SyncType.MACRO_TARGET -> "sync_macro_target"
        SyncType.DAILY_SUMMARY -> "sync_daily_summary"
        SyncType.USER_PROFILE -> "sync_user_profile"
        SyncType.WEIGHT_LOG -> "sync_weight_log"

    }
}
