package com.macrivo.app.presentation.myfood.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.macrivo.app.presentation.ui.theme.ResponsiveAppTheme
import com.macrivo.app.presentation.ui.theme.spacing

// CHANGED: new file (replaces FoodInfoHeaderCard). Shows each piece of information once:
// name, description (only when given), then one serving line.
@Composable
fun FoodPreviewCard(
    name: String,
    description: String,
    servingText: String,
    servingsPerContainer: String,
    modifier: Modifier = Modifier,
) {
    val trimmedName = name.trim()
    val trimmedDescription = description.trim()
    val servingLine = listOfNotNull(
        servingText.takeIf { it.isNotBlank() }?.let { "Serving: $it" },
        servingsPerContainer.trim().takeIf { it.isNotEmpty() }?.let { "$it per container" },
    ).joinToString(" · ")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                shape = MaterialTheme.shapes.large,
            )
            .padding(MaterialTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
    ) {
        Icon(
            imageVector = Icons.Outlined.Fastfood,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp),
        )

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = trimmedName.ifEmpty { "Food name" },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = if (trimmedName.isEmpty()) {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (trimmedDescription.isNotEmpty()) {
                Text(
                    text = trimmedDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (servingLine.isNotEmpty()) {
                Text(
                    text = servingLine,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFoodPreviewCard() {
    ResponsiveAppTheme {
        FoodPreviewCard(
            name = "Protein Bar",
            description = "Crunchy oat bar",
            servingText = "40 g",
            servingsPerContainer = "6",
            modifier = Modifier.padding(16.dp),
        )
    }
}
