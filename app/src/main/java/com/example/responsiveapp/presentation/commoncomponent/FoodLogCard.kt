package com.example.responsiveapp.presentation.commoncomponent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.ui.theme.CaloriesColor
import com.example.responsiveapp.presentation.ui.theme.CarbsColor
import com.example.responsiveapp.presentation.ui.theme.FatColor
import com.example.responsiveapp.presentation.ui.theme.ProteinColor
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun FoodLogCard(
    name: String,
    caloriesText: String,
    proteinText: String,
    carbsText: String,
    fatText: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
) {

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            },
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    if (!subtitle.isNullOrBlank()) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                trailingContent()
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.spacing.xs,
                ),
            ) {

                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = CaloriesColor,
                )

                Text(
                    text = caloriesText,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.spacing.lg,
                ),
            ) {

                MacroItem(
                    icon = Icons.Default.FitnessCenter,
                    value = proteinText,
                    tint = ProteinColor,
                )

                MacroItem(
                    icon = Icons.Default.Grain,
                    value = carbsText,
                    tint = CarbsColor,
                )

                MacroItem(
                    icon = Icons.Default.Opacity,
                    value = fatText,
                    tint = FatColor,
                )
            }
        }
    }
}
@Composable
private fun MacroItem(
    icon: ImageVector,
    value: String,
    tint: Color,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            4.dp,
        ),
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = tint,
        )

        Text(
            text = "${value}g",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = tint,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FoodLogCardTimePreview() {
    ResponsiveAppTheme {
        FoodLogCard(
            name = "Chicken Thigh",
            caloriesText = "209 kcal",
            proteinText = "26",
            carbsText = "0",
            fatText = "11",
            trailingContent = {
                Text(
                    text = "12:40 PM",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FoodLogCardAddButtonPreview() {
    ResponsiveAppTheme {
        FoodLogCard(
            name = "Pizza",
            caloriesText = "276 kcal",
            proteinText = "12",
            carbsText = "30",
            fatText = "11",
            trailingContent = {
                CircularIconButton(
                    icon = Icons.Default.Add,
                    contentDescription = "Log meal",
                    onClick = {},
                )
            },
        )
    }
}