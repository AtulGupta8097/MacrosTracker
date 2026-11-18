package com.macrivo.app.presentation.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import com.macrivo.app.R

fun SplashScreen.setBrandExitAnimation() {
    setOnExitAnimationListener { provider ->
        if (!ValueAnimator.areAnimatorsEnabled()) {
            provider.remove()
            return@setOnExitAnimationListener
        }
        provider.playExit()
    }
}

private fun SplashScreenViewProvider.playExit() {
    val splash = view as ViewGroup
    val icon: View = iconView
    val context = splash.context
    val density = context.resources.displayMetrics.density
    val background = ContextCompat.getColor(context, R.color.splash_background)
    val textColor = if (ColorUtils.calculateLuminance(background) > 0.5) Color.BLACK else Color.WHITE
    val restingY = icon.height / 2f + 28f * density
    val startY = restingY + 18f * density

    val name = TextView(context).apply {
        text = context.getString(R.string.app_name)
        setTextColor(textColor)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        letterSpacing = 0.06f
        gravity = Gravity.CENTER
        alpha = 0f
        translationY = startY
    }
    splash.addView(
        name,
        FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        )
    )

    val pop = AnimatorSet().apply {
        playTogether(
            ObjectAnimator.ofFloat(icon, View.SCALE_X, 1f, 1.12f),
            ObjectAnimator.ofFloat(icon, View.SCALE_Y, 1f, 1.12f),
        )
        duration = POP_MS
        interpolator = OvershootInterpolator(2f)
    }

    val nameIn = AnimatorSet().apply {
        playTogether(
            ObjectAnimator.ofFloat(name, View.ALPHA, 0f, 1f),
            ObjectAnimator.ofFloat(name, View.TRANSLATION_Y, startY, restingY),
        )
        startDelay = NAME_DELAY_MS
        duration = NAME_MS
        interpolator = DecelerateInterpolator(1.6f)
    }

    val intro = AnimatorSet().apply {
        playTogether(pop, nameIn)
    }

    val leave = AnimatorSet().apply {
        playTogether(
            ObjectAnimator.ofFloat(icon, View.SCALE_X, 1.12f, 0f),
            ObjectAnimator.ofFloat(icon, View.SCALE_Y, 1.12f, 0f),
            ObjectAnimator.ofFloat(icon, View.ALPHA, 1f, 0f),
            ObjectAnimator.ofFloat(name, View.ALPHA, 1f, 0f),
            ObjectAnimator.ofFloat(splash, View.ALPHA, 1f, 0f),
        )
        startDelay = HOLD_MS
        duration = LEAVE_MS
        interpolator = AccelerateInterpolator(1.4f)
    }

    AnimatorSet().apply {
        playSequentially(intro, leave)
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                remove()
            }
        })
        start()
    }
}

private const val POP_MS = 280L
private const val NAME_DELAY_MS = 120L
private const val NAME_MS = 380L
private const val HOLD_MS = 220L
private const val LEAVE_MS = 360L