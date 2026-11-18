package com.macrivo.app.core.ads

import android.app.Activity
import android.content.Context
import android.os.SystemClock
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

sealed interface RewardedAdState {
    data object Idle : RewardedAdState
    data object Loading : RewardedAdState
    data object Ready : RewardedAdState
    data object Showing : RewardedAdState
    data class Failed(val message: String) : RewardedAdState
}

sealed interface RewardedAdResult {
    data object Earned : RewardedAdResult
    data object DismissedWithoutReward : RewardedAdResult
    data object NotReady : RewardedAdResult
    data class FailedToShow(val message: String) : RewardedAdResult
}


@Singleton
class RewardedAdManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val _state = MutableStateFlow<RewardedAdState>(RewardedAdState.Idle)
    val state: StateFlow<RewardedAdState> = _state.asStateFlow()

    private var ad: RewardedAd? = null
    private var loadedAtMs = 0L

    fun load() {
        when (_state.value) {
            RewardedAdState.Loading, RewardedAdState.Showing -> return
            RewardedAdState.Ready -> if (!isExpired()) return else discardAd()
            else -> Unit
        }

        _state.value = RewardedAdState.Loading
        RewardedAd.load(
            context,
            AdConfig.REWARDED_AD_UNIT_ID,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(loaded: RewardedAd) {
                    ad = loaded
                    loadedAtMs = SystemClock.elapsedRealtime()
                    _state.value = RewardedAdState.Ready
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.w(TAG, "load failed: ${error.code}")
                    ad = null
                    _state.value = RewardedAdState.Failed(loadFailureMessage(error))
                }
            },
        )
    }

    fun show(activity: Activity, onResult: (RewardedAdResult) -> Unit) {
        val current = ad
        if (current == null || _state.value != RewardedAdState.Ready || isExpired()) {
            discardAd()
            onResult(RewardedAdResult.NotReady)
            load()
            return
        }

        ad = null // an ad can be shown only once
        _state.value = RewardedAdState.Showing

        var rewardEarned = false
        var finished = false
        fun finish(result: RewardedAdResult) {
            if (finished) return
            finished = true
            _state.value = RewardedAdState.Idle
            onResult(result)
            load() // get the next ad ready
        }

        current.setServerSideVerificationOptions(
            ServerSideVerificationOptions.Builder()
                .setCustomData(UUID.randomUUID().toString())
                .build()
        )
        current.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                finish(
                    if (rewardEarned) RewardedAdResult.Earned
                    else RewardedAdResult.DismissedWithoutReward
                )
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.w(TAG, "show failed: ${error.code}")
                finish(RewardedAdResult.FailedToShow("The ad couldn't be shown. Please try again."))
            }
        }

        try {
            current.show(activity) { rewardEarned = true }
        } catch (e: Exception) {
            Log.w(TAG, "show threw", e)
            finish(RewardedAdResult.FailedToShow("The ad couldn't be shown. Please try again."))
        }
    }

    private fun isExpired() = SystemClock.elapsedRealtime() - loadedAtMs > AD_EXPIRY_MS

    private fun discardAd() {
        ad = null
        if (_state.value == RewardedAdState.Ready) _state.value = RewardedAdState.Idle
    }

    private fun loadFailureMessage(error: LoadAdError) = when (error.code) {
        AdRequest.ERROR_CODE_NETWORK_ERROR -> "No internet connection. Connect and try again."
        AdRequest.ERROR_CODE_NO_FILL -> "No ad is available right now. Please try again in a moment."
        else -> "Couldn't load the ad. Please try again."
    }

    private companion object {
        const val TAG = "RewardedAdManager"
        const val AD_EXPIRY_MS = 55 * 60 * 1000L
    }
}
