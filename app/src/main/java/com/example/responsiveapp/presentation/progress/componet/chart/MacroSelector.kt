package com.example.responsiveapp.presentation.progress.componet.chart

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.responsiveapp.presentation.progress.model.MacroType
import com.example.responsiveapp.presentation.ui.theme.CarbsColor
import com.example.responsiveapp.presentation.ui.theme.FatColor
import com.example.responsiveapp.presentation.ui.theme.ProteinColor
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun MacroSelector(
    selected: MacroType,
    onMacroSelected: (MacroType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(50))
            .padding(MaterialTheme.spacing.xxs),
    ) {
        MacroOption(
            label = "Protein",
            color = ProteinColor,
            selected = selected == MacroType.PROTEIN,
            onClick = { onMacroSelected(MacroType.PROTEIN) },
            modifier = Modifier.weight(1f),
        )
        MacroOption(
            label = "Carbs",
            color = CarbsColor,
            selected = selected == MacroType.CARBS,
            onClick = { onMacroSelected(MacroType.CARBS) },
            modifier = Modifier.weight(1f),
        )
        MacroOption(
            label = "Fats",
            color = FatColor,
            selected = selected == MacroType.FATS,
            onClick = { onMacroSelected(MacroType.FATS) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun MacroOption(
    label: String,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val background by animateColorAsState(
        targetValue = if (selected) color else Color.Transparent,
        label = "macroOptionBackground",
    )
    val contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

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