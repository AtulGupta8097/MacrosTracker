package com.example.responsiveapp.presentation.progress.componet.weight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.commoncomponent.CustomButton
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun WeightEmptyState(
    onLogWeightClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val spacing = MaterialTheme.spacing

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(28.dp),
                clip = false
            ),
        shape = RoundedCornerShape(28.dp),
        color = colorScheme.surface,
        tonalElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = spacing.lg,
                    vertical = spacing.xl
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            // Icon / visual focal point
            Box(
                modifier = Modifier.size(108.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(108.dp)
                        .background(
                            color = colorScheme.primary.copy(alpha = 0.05f),
                            shape = CircleShape
                        )
                )

                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .background(
                            color = colorScheme.primary.copy(alpha = 0.10f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                color = colorScheme.primary.copy(alpha = 0.16f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MonitorWeight,
                            contentDescription = null,
                            tint = colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.size(spacing.lg))

            Text(
                text = "Start tracking your weight",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.size(spacing.xs))

            Text(
                text = "Your progress starts with a single entry. Log your weight to begin building your journey.",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.size(spacing.lg))

            CustomButton(
                text = "Log Weight",
                onClick = onLogWeightClicked,
                imageVector = Icons.Outlined.Add,
                modifier = Modifier.fillMaxWidth(),
                buttonColors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary,
                    contentColor = colorScheme.onPrimary,
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrevWeightEmptyState() {
    ResponsiveAppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            WeightEmptyState(
                onLogWeightClicked = {}
            )
        }
    }
}