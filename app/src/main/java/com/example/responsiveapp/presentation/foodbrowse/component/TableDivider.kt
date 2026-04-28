package com.example.responsiveapp.presentation.foodbrowse.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TableDivider(thick: Boolean = false) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(if (thick) 3.dp else 0.5.dp)
            .background(
                if (thick) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
    )
}