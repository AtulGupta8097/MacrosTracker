package com.example.responsiveapp.presentation.home.componet.nutrition

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun NutritionInfo(
    modifier: Modifier = Modifier,
    consumedText: String,
    targetText: String,
    remainingText: String,
) {

    Column(
        modifier = modifier,
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
        ) {

            Text(
                text = consumedText,
                style = MaterialTheme.typography.headlineMedium
                    .copy(
                        fontWeight = FontWeight.Bold,
                    ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )

            Text(
                text = targetText,
                modifier = Modifier
                    .weight(1f, fill = false)
                    .padding(
                        bottom = MaterialTheme.spacing.xxs,
                    ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Text(
            text = remainingText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
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
            consumedText = "1850",
            targetText = " / 2400 kcal",
            remainingText = "550 kcal left",
        )
    }
}