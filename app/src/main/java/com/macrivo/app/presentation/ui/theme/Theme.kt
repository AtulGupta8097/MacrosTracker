
package com.macrivo.app.presentation.ui.theme

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

// --------------------------------------------------
// DARK COLOR SCHEME
// MIDNIGHT EMBER
// --------------------------------------------------

private val DarkColorScheme = darkColorScheme(

    // Primary - Brand / CTA
    primary = FitnessOrangeDarkMode,
    onPrimary = Color(0xFF241106),
    primaryContainer = FitnessOrangeContainerDark,
    onPrimaryContainer = Color(0xFFFFDCC7),
    inversePrimary = FitnessOrangeLight,

    // Secondary - Neutral actions
    secondary = SecondaryDark,
    onSecondary = Color(0xFF302A27),
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = Color(0xFFF1E7E0),

    // Tertiary - Success / Wellness
    tertiary = FitnessGreenDarkMode,
    onTertiary = Color(0xFF07351B),
    tertiaryContainer = SuccessContainerDark,
    onTertiaryContainer = Color(0xFFB9F6CE),

    // Background
    background = DarkBackground,
    onBackground = OnDarkPrimary,

    // Surface
    surface = DarkSurface,
    onSurface = OnDarkPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = OnDarkSecondary,
    surfaceTint = FitnessOrangeDarkMode,

    // Inverse surfaces
    inverseSurface = Color(0xFFF1ECE8),
    inverseOnSurface = Color(0xFF302A27),

    // Borders
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,

    // Error
    error = ErrorRedDark,
    onError = Color(0xFF690005),
    errorContainer = ErrorContainerDark,
    onErrorContainer = Color(0xFFFFDAD6),

    scrim = Black
)

// --------------------------------------------------
// LIGHT COLOR SCHEME
// MIDNIGHT EMBER
// --------------------------------------------------

private val LightColorScheme = lightColorScheme(

    // Primary - Brand / CTA
    primary = FitnessOrangeLight,
    onPrimary = White,
    primaryContainer = FitnessOrangeContainerLight,
    onPrimaryContainer = Color(0xFF5A2611),
    inversePrimary = FitnessOrangeDarkMode,

    // Secondary - Neutral actions
    secondary = SecondaryLight,
    onSecondary = White,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = Color(0xFF292321),

    // Tertiary - Success / Wellness
    tertiary = FitnessGreen,
    onTertiary = White,
    tertiaryContainer = SuccessContainerLight,
    onTertiaryContainer = Color(0xFF104B2C),

    // Background
    background = LightBackground,
    onBackground = LightOnBackground,

    // Surface
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    surfaceTint = FitnessOrangeLight,

    // Inverse surfaces
    inverseSurface = Color(0xFF302A27),
    inverseOnSurface = Color(0xFFF7F2EE),

    // Borders
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,

    // Error
    error = ErrorRed,
    onError = White,
    errorContainer = ErrorContainerLight,
    onErrorContainer = Color(0xFF410002),

    scrim = Black
)

// --------------------------------------------------
// COMPOSITION LOCALS
// --------------------------------------------------

val LocalSpacing = staticCompositionLocalOf { Spacing() }

// --------------------------------------------------
// APP THEME
// --------------------------------------------------

@Composable
fun ResponsiveAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = when {

        dynamicColor &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {

            val context = LocalContext.current

            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
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
