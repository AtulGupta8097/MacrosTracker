package com.example.responsiveapp.presentation.foodbrowse.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun NutritionRow(
    label: String,
    value: String,
    isHeader: Boolean = false,
    indent: Boolean = false,
) {
    val labelStyle = if (isHeader)
        MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
    else
        MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = if (indent) 28.dp else MaterialTheme.spacing.md,
                end = MaterialTheme.spacing.md,
                top = if (isHeader) 10.dp else 7.dp,
                bottom = if (isHeader) 10.dp else 7.dp,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = labelStyle)

        if (isHeader) {
            AnimatedContent(
                targetState = value,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "${label}ValueAnim"
            ) { v ->
                Text(text = v, style = labelStyle)
            }
        } else {
            Text(text = value, style = labelStyle)
        }
    }
}
