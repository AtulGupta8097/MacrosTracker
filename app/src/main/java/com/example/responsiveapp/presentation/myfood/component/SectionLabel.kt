package com.example.responsiveapp.presentation.myfood.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun SectionLabel(
    text: String,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
        ),
        modifier = Modifier.padding(
            start = MaterialTheme.spacing.sm,
        ),
    )
}