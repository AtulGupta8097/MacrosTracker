package com.example.responsiveapp.presentation.mainscreen

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class AddFoodOption(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconColor: Color,
    val backgroundColor: Color,
)