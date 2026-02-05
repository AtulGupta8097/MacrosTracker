package com.example.responsiveapp.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(

    // 🟧 PRIMARY (CTA)
    primary = FitnessOrangeDarkMode,
    onPrimary = DarkBackground,
    primaryContainer = FitnessOrangeContainerDark,
    onPrimaryContainer = OnDarkPrimary,

    // ⚫ SECONDARY
    secondary = DarkSurfaceVariant,
    onSecondary = OnDarkPrimary,

    // 🟩 TERTIARY
    tertiary = FitnessGreenDarkMode,
    onTertiary = DarkBackground,

    // 🔵 INFO / AUTH
    secondaryContainer = FitnessBlueDarkMode,
    onSecondaryContainer = DarkBackground,

    // 🌑 BACKGROUND
    background = DarkBackground,
    onBackground = OnDarkPrimary,

    // 🧱 SURFACE
    surface = DarkSurface,
    onSurface = OnDarkPrimary,

    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = OnDarkSecondary,

    // ❌ ERROR
    error = ErrorRedDark,
    onError = DarkBackground
)

private val LightColorScheme = lightColorScheme(

    // 🟧 PRIMARY (CTA)
    primary = FitnessOrange,
    onPrimary = CardBackground,
    primaryContainer = primaryVarient,


    // ⚫ SECONDARY (Login / Nav / Important Actions)
    secondary = DarkNavy,
    onSecondary = CardBackground,

    // 🟩 TERTIARY (Success / Nutrition)
    tertiary = FitnessGreen,
    onTertiary = CardBackground,

    // 🔵 AUTH / INFO ACTIONS
    secondaryContainer = FitnessBlue,
    onSecondaryContainer = CardBackground,

    // ⚪ BACKGROUND
    background = Color.White,
    onBackground = DarkNavy,

    // 🧱 SURFACE (Cards, Inputs)
    surface = CardBackground,
    onSurface = DarkNavy,
    surfaceVariant = Surfacevarient,

    // ❌ ERROR
    error = ErrorRed,
    onError = CardBackground
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }
@Composable
fun ResponsiveAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val adaptiveInfo = currentWindowAdaptiveInfo()

    val deviceConfiguration =
        DeviceConfiguration.fromAdaptiveInfo(adaptiveInfo)

    CompositionLocalProvider(
        LocalSpacing provides Spacing(),
        LocalDeviceConfiguration provides deviceConfiguration
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }

}