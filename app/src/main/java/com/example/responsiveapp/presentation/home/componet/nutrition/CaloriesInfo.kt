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
fun CaloriesInfo(
    consumedCalories: Float,
    targetCalories: Int,
    modifier: Modifier = Modifier,
) {
    val remaining =
        (targetCalories - consumedCalories)
            .roundToInt()

    Column(
        modifier = modifier,
    ) {

        Row(
            verticalAlignment = Alignment.Bottom,
        ) {

            Text(
                text = consumedCalories.roundToInt().toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = " / $targetCalories kcal",
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
                    "$remaining kcal left"
                } else {
                    "${-remaining} kcal over"
                },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(
                top = MaterialTheme.spacing.xxs,
            ),
        )
    }
}

@Preview
@Composable
private fun PrevCaloriesInfo() {
    ResponsiveAppTheme {
        CaloriesInfo(
            consumedCalories = 320f,
            targetCalories = 300,
        )
    }
}