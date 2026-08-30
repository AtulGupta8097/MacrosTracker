package com.example.responsiveapp.presentation.progress.componet.chart

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.progress.model.MacroType
import com.example.responsiveapp.presentation.ui.theme.CaloriesColor
import com.example.responsiveapp.presentation.ui.theme.CarbsColor
import com.example.responsiveapp.presentation.ui.theme.FatColor
import com.example.responsiveapp.presentation.ui.theme.ProteinColor
import com.example.responsiveapp.presentation.ui.theme.spacing

private data class MacroChipSpec(
    val type: MacroType,
    val label: String,
    val icon: ImageVector,
    val color: Color,
)

@Composable
fun MacroSelector(
    selected: MacroType,
    onMacroSelected: (MacroType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = remember {
        listOf(
            MacroChipSpec(MacroType.CALORIES, "Calories", Icons.Filled.LocalFireDepartment, CaloriesColor),
            MacroChipSpec(MacroType.PROTEIN, "Protein", Icons.Filled.FitnessCenter, ProteinColor),
            MacroChipSpec(MacroType.CARBS, "Carbs", Icons.Filled.Grain, CarbsColor),
            MacroChipSpec(MacroType.FATS, "Fat", Icons.Filled.Opacity, FatColor),
        )
    }

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(MaterialTheme.spacing.sm),
        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.xxs),
    ) {
        items(options, key = { it.type }) { option ->
            MacroChip(
                spec = option,
                selected = option.type == selected,
                onClick = { onMacroSelected(option.type) },
            )
        }
    }
}

@Composable
private fun MacroChip(
    spec: MacroChipSpec,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) spec.color else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        label = "chipBackground",
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "chipContent",
    )
    val scale by animateFloatAsState(
        targetValue = if (selected) 1f else 0.95f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "chipScale",
    )
    val elevation by animateDpAsState(
        targetValue = if (selected) 6.dp else 0.dp,
        label = "chipElevation",
    )

    Surface(
        onClick = onClick,
        modifier = Modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        shape = RoundedCornerShape(50),
        color = backgroundColor,
        shadowElevation = elevation,
        tonalElevation = if (selected) 2.dp else 0.dp,
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = MaterialTheme.spacing.md,
                vertical = MaterialTheme.spacing.sm,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = spec.icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = spec.label,
                modifier = Modifier.padding(start = MaterialTheme.spacing.xs),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = contentColor,
            )
        }
    }
}