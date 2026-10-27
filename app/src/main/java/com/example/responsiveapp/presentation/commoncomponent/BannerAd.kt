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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAd(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val adWidthDp = maxWidth.value.toInt()

        if (adWidthDp > 0) {
            key(context, adWidthDp) {
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),

                    factory = { viewContext ->
                        AdView(viewContext).apply {
                            adUnitId = AdConfig.BANNER_AD_UNIT_ID

                            setAdSize(AdSize.getInlineAdaptiveBannerAdSize(
                                adWidthDp,
                                80
                            ))


                            loadAd(AdRequest.Builder().build())
                        }
                    },

                    onReset = {
                        // No transient View state to reset.
                    },

                    onRelease = { adView ->
                        Log.d("BannerAd", "AdView released")
                        adView.destroy()
                    }
                )
            }
        }
    }
}