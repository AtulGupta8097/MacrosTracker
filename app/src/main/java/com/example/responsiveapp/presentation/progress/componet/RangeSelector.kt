package com.example.responsiveapp.presentation.progress.componet

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.responsiveapp.presentation.progress.model.ProgressRange
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun RangeSelector(
    selected: ProgressRange,
    onRangeSelected: (ProgressRange) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(50),
            )
            .padding(MaterialTheme.spacing.xxs)
    ) {
        RangeOption(
            label = "Last 7 Days",
            selected = selected == ProgressRange.WEEK,
            onClick = { onRangeSelected(ProgressRange.WEEK) },
            modifier = Modifier.weight(1f),
        )
        RangeOption(
            label = "This Month",
            selected = selected == ProgressRange.MONTH,
            onClick = { onRangeSelected(ProgressRange.MONTH) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun RangeOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val background by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surface
        },
        label = "rangeOptionBackground",
    )

    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .clickable(onClick = onClick)
            .padding(vertical = MaterialTheme.spacing.sm),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = contentColor,
            textAlign = TextAlign.Center,
        )
    }
}
