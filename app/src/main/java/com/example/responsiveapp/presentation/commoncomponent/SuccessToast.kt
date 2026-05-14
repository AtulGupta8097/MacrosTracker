package com.example.responsiveapp.presentation.commoncomponent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.responsiveapp.domain.model.CustomToastProperty

class SuccessToast : CustomToastProperty {

    override fun icon() = Icons.Default.CheckCircle

    @Composable
    override fun backgroundColor(): Color =
        MaterialTheme.colorScheme.surface

    @Composable
    override fun borderColor(): Color =
        MaterialTheme.colorScheme.tertiary

    @Composable
    override fun textColor(): Color =
        MaterialTheme.colorScheme.onSurface

    @Composable
    override fun progressBarColor(): Color =
        MaterialTheme.colorScheme.tertiary
}