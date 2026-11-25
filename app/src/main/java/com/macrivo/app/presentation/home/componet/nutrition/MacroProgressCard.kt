package com.macrivo.app.presentation.home.componet.nutrition

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.macrivo.app.presentation.home.model.MacroProgressUiModel
import com.macrivo.app.presentation.ui.theme.CarbsColor
import com.macrivo.app.presentation.ui.theme.ResponsiveAppTheme
import com.macrivo.app.presentation.ui.theme.spacing

@Composable
fun MacroProgressCard(
    macro: MacroProgressUiModel,
    modifier: Modifier = Modifier,
    onEditClick: (() -> Unit)? = null,
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
        ),
    ) {

        Column(
            modifier = Modifier.padding(
                MaterialTheme.spacing.md,
            ),
            verticalArrangement = Arrangement.spacedBy(
                MaterialTheme.spacing.sm,
            ),
        ) {

            MacroHeader(
                icon = macro.icon,
                accentColor = macro.accentColor,
                label = macro.label,
                onEditClick = onEditClick,
            )

            Text(
                text = macro.label,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium
                    .copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            NutritionInfo(
                consumedText = macro.consumedText,
                targetText = macro.targetText,
                remainingText = macro.remainingText,
            )

            LinearProgress(
                progress = macro.progress,
                progressColor = macro.accentColor,
                trackColor = macro.accentColor.copy(alpha = 0.14f),
            )
        }
    }
}

@Composable
private fun MacroHeader(
    icon: ImageVector,
    accentColor: Color,
    label: String,
    onEditClick: (() -> Unit)?,
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    accentColor.copy(alpha = 0.14f),
                ),
            contentAlignment = Alignment.Center,
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(20.dp),
            )
        }

        if (onEditClick != null) {
            EditTargetButton(
                contentDescription = "Edit $label target",
                onClick = onEditClick,
                tint = accentColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MacroProgressCardPreview() {
    ResponsiveAppTheme {
        MacroProgressCard(
            macro = MacroProgressUiModel(
                key = "carbs",
                targetValue = 220,
                icon = Icons.Default.Grain,
                label = "Carbs",
                consumedText = "145",
                targetText = " / 220 g",
                remainingText = "75 g left",
                progress = 0.66f,
                accentColor = CarbsColor,
            ),
            onEditClick = {},
        )
    }
}
