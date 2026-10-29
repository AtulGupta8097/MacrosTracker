
package com.example.responsiveapp.presentation.commoncomponent

import android.util.Log
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.responsiveapp.core.ads.AdConfig
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

private const val TAG = "BannerAd"

@Composable
fun BannerAd(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val adWidthDp = maxWidth.value.toInt()

        Log.d(TAG, "Container width: ${adWidthDp}dp")

        if (adWidthDp > 0) {
            key(context, adWidthDp) {
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),

                    factory = { viewContext ->
                        Log.d(TAG, "Creating AdView")
                        Log.d(
                            TAG,
                            "Ad unit ID: ${AdConfig.BANNER_AD_UNIT_ID}"
                        )

                        AdView(viewContext).apply {
                            adUnitId = AdConfig.BANNER_AD_UNIT_ID

                            setAdSize(
                                AdSize.getInlineAdaptiveBannerAdSize(
                                    adWidthDp,
                                    80
                                )
                            )

                            adListener = object : AdListener() {

                                override fun onAdLoaded() {
                                    Log.i(
                                        TAG,
                                        "SUCCESS: Banner ad loaded"
                                    )
                                }

                                override fun onAdFailedToLoad(
                                    error: LoadAdError
                                ) {
                                    Log.e(
                                        TAG,
                                        """
                                        AD LOAD FAILED
                                        Code: ${error.code}
                                        Message: ${error.message}
                                        Domain: ${error.domain}
                                        Response info: ${error.responseInfo}
                                        """.trimIndent()
                                    )
                                }

                                override fun onAdImpression() {
                                    Log.d(
                                        TAG,
                                        "Ad impression recorded"
                                    )
                                }

                                override fun onAdClicked() {
                                    Log.d(TAG, "Ad clicked")
                                }

                                override fun onAdOpened() {
                                    Log.d(TAG, "Ad opened")
                                }

                                override fun onAdClosed() {
                                    Log.d(TAG, "Ad closed")
                                }
                            }

                            val request = AdRequest.Builder().build()

                            Log.d(TAG, "Starting loadAd()")
                            loadAd(request)
                        }
                    },

                    onReset = {
                        Log.d(TAG, "AdView reset")
                    },

                    onRelease = { adView ->
                        Log.d(TAG, "Destroying AdView")
                        adView.destroy()
                    }
                )
            }
        } else {
            Log.d(TAG, "Waiting for a valid container width")
        }
    }
}
