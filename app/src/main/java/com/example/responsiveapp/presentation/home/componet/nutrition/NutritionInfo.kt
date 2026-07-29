package com.example.responsiveapp.presentation.home.componet.nutrition

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing
import kotlin.math.roundToInt

@Composable
fun NutritionInfo(
    consumed: Float,
    target: Int,
    unit: String,
    modifier: Modifier = Modifier,
) {
    val remaining =
        (target - consumed)
            .roundToInt()

    Column(
        modifier = modifier,
    ) {

        Row(
            verticalAlignment = Alignment.Bottom,
        ) {

            Text(
                text = consumed.roundToInt().toString(),
                style = MaterialTheme.typography.headlineMedium
                    .copy(
                        fontWeight = FontWeight.Bold,
                    ),
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = " / $target $unit",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(
                    bottom = MaterialTheme.spacing.xxs,
                ),
            )
        }

        Text(
            text =
                if (remaining >= 0) {
                    "$remaining $unit left"
                } else {
                    "${-remaining} $unit over"
                },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(
                top = MaterialTheme.spacing.xxs,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NutritionInfoPreview() {
    ResponsiveAppTheme {
        NutritionInfo(
            consumed = 1_850f,
            target = 2_400,
            unit = "kcal",
        )
    }
}