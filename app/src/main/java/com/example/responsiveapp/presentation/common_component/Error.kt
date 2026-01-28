package com.example.responsiveapp.presentation.common_component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.responsiveapp.domain.model.CustomToastProperty


class ErrorToast : CustomToastProperty{

    override fun icon() = Icons.Default.Error

    @Composable
    override fun backgroundColor(): Color =
        MaterialTheme.colorScheme.surface

    @Composable
    override fun borderColor(): Color =
        MaterialTheme.colorScheme.error

    @Composable
    override fun textColor(): Color =
        MaterialTheme.colorScheme.onErrorContainer

    @Composable
    override fun progressBarColor(): Color =
        MaterialTheme.colorScheme.error
}