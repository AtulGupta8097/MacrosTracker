package com.example.responsiveapp.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

interface CustomToastProperty {

    fun icon(): ImageVector

    @Composable
    fun backgroundColor(): Color

    @Composable
    fun borderColor(): Color

    @Composable
    fun textColor(): Color

    @Composable
    fun progressBarColor(): Color
}