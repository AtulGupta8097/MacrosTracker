package com.example.responsiveapp.presentation.mymeal.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.responsiveapp.presentation.foodbrowse.component.MacroCard
import com.example.responsiveapp.presentation.ui.theme.CarbsColor
import com.example.responsiveapp.presentation.ui.theme.FatColor
import com.example.responsiveapp.presentation.ui.theme.ProteinColor
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun MacroSummarySection(
    modifier: Modifier = Modifier,
    totalCal: Float,
    totalProtein: Float,
    totalCarbs: Float,
    totalFat: Float,
) {
    val primary = MaterialTheme.colorScheme.primary
    val spacing = MaterialTheme.spacing

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.92f),
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Box {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(primary.copy(alpha = 0.14f), Color.Transparent)
                        )
                    )
            )

            Column(
                modifier = Modifier.padding(spacing.lg)
            ) {
                Text(
                    text = "TOTAL CALORIES",
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 2.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                )

                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(top = spacing.sm)
                ) {
                    Text(
                        text = totalCal.toInt().toString(),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 90.sp
                        )
                    )
                    Text(
                        text = " kcal",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(bottom = 14.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing.lg)
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.55f))
                        .padding(vertical = spacing.md, horizontal = spacing.sm),
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm)
                ) {
                    MacroCard(
                        label = "Protein",
                        value = totalProtein,
                        icon = Icons.Default.FitnessCenter,
                        accentColor = ProteinColor,
                        modifier = Modifier.weight(1f)
                    )
                    MacroCard(
                        label = "Carbs",
                        value = totalCarbs,
                        icon = Icons.Default.BakeryDining,
                        accentColor = CarbsColor,
                        modifier = Modifier.weight(1f)
                    )
                    MacroCard(
                        label = "Fat",
                        value = totalFat,
                        icon = Icons.Default.Opacity,
                        accentColor = FatColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PrevMacroSummary() {
    ResponsiveAppTheme(darkTheme = true) {
        MacroSummarySection(
            totalCal = 480f,
            totalProtein = 10.2f,
            totalCarbs = 60.1f,
            totalFat = 38.8f,
        )
    }
}