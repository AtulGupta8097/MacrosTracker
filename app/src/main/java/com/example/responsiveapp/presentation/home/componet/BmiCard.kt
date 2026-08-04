package com.example.responsiveapp.presentation.home.componet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.health.BmiStatus
import com.example.responsiveapp.presentation.ui.theme.ErrorRed
import com.example.responsiveapp.presentation.ui.theme.FitnessBlue
import com.example.responsiveapp.presentation.ui.theme.FitnessGreen
import com.example.responsiveapp.presentation.ui.theme.FitnessOrange
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

private const val ContentWeight = 0.72f
private const val IllustrationWeight = 0.28f

@Composable
fun BmiCard(
    bmiText: String,
    bmiStatus: BmiStatus,
    modifier: Modifier = Modifier,
) {
    val statusColor = bmiStatus.toColor()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Column(
                modifier = Modifier.weight(ContentWeight),
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Body Mass Index",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Box(
                        modifier = Modifier
                            .padding(start = MaterialTheme.spacing.xs)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(50),
                            )
                            .padding(
                                horizontal = MaterialTheme.spacing.xs,
                                vertical = 2.dp,
                            ),
                    ) {
                        Text(
                            text = "BMI",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }

                Row(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.sm),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = bmiText,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = " kg/m²",
                        modifier = Modifier.padding(
                            bottom = MaterialTheme.spacing.xxs,
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Row(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Box(
                        modifier = Modifier.background(
                            color = statusColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(50),
                        ),
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = MaterialTheme.spacing.sm,
                                vertical = MaterialTheme.spacing.xxs,
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(
                                        color = statusColor,
                                        shape = CircleShape,
                                    ),
                            )

                            Text(
                                text = bmiStatus.label,
                                modifier = Modifier.padding(
                                    start = MaterialTheme.spacing.xxs,
                                ),
                                style = MaterialTheme.typography.labelMedium,
                                color = statusColor,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }

                    VerticalDivider(
                        modifier = Modifier
                            .padding(horizontal = MaterialTheme.spacing.sm)
                            .size(
                                width = 1.dp,
                                height = 24.dp,
                            ),
                    )

                    Column {
                        Text(
                            text = "Normal range",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                        Text(
                            text = "18.5 – 24.9",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }

            BodyIllustration(
                modifier = Modifier.weight(IllustrationWeight),
            )
        }
    }
}

@Composable
private fun BmiStatus.toColor(): Color = when (this) {
    BmiStatus.UNDERWEIGHT -> FitnessBlue
    BmiStatus.HEALTHY -> FitnessGreen
    BmiStatus.OVERWEIGHT -> FitnessOrange
    BmiStatus.OBESE -> ErrorRed
    BmiStatus.UNKNOWN -> MaterialTheme.colorScheme.onSurfaceVariant
}

@Preview(showBackground = true)
@Composable
private fun PrevBmiCard() {
    ResponsiveAppTheme {
        BmiCard(
            bmiText = "24.2",
            bmiStatus = BmiStatus.HEALTHY,
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 700,
)
@Composable
private fun PrevBmiCardTablet() {
    ResponsiveAppTheme {
        BmiCard(
            bmiText = "24.2",
            bmiStatus = BmiStatus.HEALTHY,
        )
    }
}