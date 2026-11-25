package com.macrivo.app.presentation.commoncomponent

import android.content.Context
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.MainThread
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.macrivo.app.core.ads.AdConfig
import kotlin.math.abs

private const val TAG = "BannerAd"
private const val DEFAULT_PLACEMENT = "main"
private const val MAX_BANNER_HEIGHT_DP = 80

private const val WIDTH_TOLERANCE_DP = 16
private const val MIN_RESIZE_RELOAD_MS = 30_000L


class BannerAdController(private val context: Context) {

    private class Entry(val view: AdView, val widthDp: Int, val createdAtMs: Long)

    private val entries = HashMap<String, Entry>()
    private var destroyed = false
    private var paused = false

    @MainThread
    fun obtain(placement: String, widthDp: Int): AdView? {
        if (destroyed || widthDp <= 0) return null

        val existing = entries[placement]
        if (existing != null) {
            val widthChanged = abs(existing.widthDp - widthDp) > WIDTH_TOLERANCE_DP
            val oldEnough = SystemClock.elapsedRealtime() - existing.createdAtMs >= MIN_RESIZE_RELOAD_MS
            if (!widthChanged || !oldEnough) return existing.view
            release(existing.view) // size really changed: an adaptive banner needs a new request for it
        }

        val view = AdView(context).apply {
            adUnitId = AdConfig.BANNER_AD_UNIT_ID
            setAdSize(AdSize.getInlineAdaptiveBannerAdSize(widthDp, MAX_BANNER_HEIGHT_DP))
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    Log.d(TAG, "[$placement] banner loaded")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.w(TAG, "[$placement] load failed: code=${error.code} ${error.message}")
                }
            }
            if (paused) pause()
            loadAd(AdRequest.Builder().build()) // the only loadAd() call in the app
        }
        entries[placement] = Entry(view, widthDp, SystemClock.elapsedRealtime())
        return view
    }

    fun pause() {
        paused = true
        entries.values.forEach { it.view.pause() }
    }

    fun resume() {
        paused = false
        entries.values.forEach { it.view.resume() }
    }

    fun destroy() {
        destroyed = true
        entries.values.forEach { release(it.view) }
        entries.clear()
    }

    private fun release(view: AdView) {
        (view.parent as? ViewGroup)?.removeView(view)
        view.destroy()
    }
}


val LocalBannerAdController = staticCompositionLocalOf<BannerAdController?> { null }


@Composable
fun rememberBannerAdController(): BannerAdController {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val controller = remember(context) { BannerAdController(context) }

    DisposableEffect(controller, lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> controller.pause()
                Lifecycle.Event.ON_RESUME -> controller.resume()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            controller.destroy()
        }
    }
    return controller
}

@Composable
fun BannerAd(
    modifier: Modifier = Modifier,
    placement: String = DEFAULT_PLACEMENT,
) {
    if (LocalInspectionMode.current) return


    val controller = LocalBannerAdController.current ?: rememberBannerAdController()

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val adWidthDp = maxWidth.value.toInt()

        if (adWidthDp > 0) {
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { viewContext -> FrameLayout(viewContext) },
                update = { container ->
                    // only moves the existing AdView into this container; it never requests an ad
                    val adView = controller.obtain(placement, adWidthDp)
                    if (adView == null) {
                        container.removeAllViews()
                    } else if (container.getChildAt(0) !== adView) {
                        container.removeAllViews()
                        (adView.parent as? ViewGroup)?.removeView(adView)
                        container.addView(
                            adView,
                            FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                Gravity.CENTER_HORIZONTAL,
                            ),
                        )
                    }
                },
                // detach only; the controller owns the AdView and destroys it
                onRelease = { container -> container.removeAllViews() },
            )
        }
    }
}
