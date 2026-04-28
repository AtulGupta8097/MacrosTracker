package com.example.responsiveapp.presentation.commoncomponent

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun isExpandedWidth(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp >= 600
}
