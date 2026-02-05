package com.example.responsiveapp.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

enum class DeviceConfiguration {
    MOBILE,
    TABLET,
    DESKTOP;

    companion object {

        fun fromAdaptiveInfo(
            adaptiveInfo: WindowAdaptiveInfo
        ): DeviceConfiguration {

            val windowSizeClass = adaptiveInfo.windowSizeClass

            return when {
                // Desktop / large screens
                windowSizeClass.isWidthAtLeastBreakpoint(840) ->
                    DESKTOP

                // Tablets
                windowSizeClass.isWidthAtLeastBreakpoint(600) ->
                    TABLET

                // Phones
                else ->
                    MOBILE
            }
        }
    }
}
val LocalDeviceConfiguration =
    staticCompositionLocalOf {
        DeviceConfiguration.MOBILE
    }


val MaterialTheme.deviceConfiguration: DeviceConfiguration
    @Composable
    @ReadOnlyComposable
    get() = LocalDeviceConfiguration.current



