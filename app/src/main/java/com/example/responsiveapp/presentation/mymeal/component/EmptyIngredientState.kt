package com.example.responsiveapp.presentation.mymeal.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoMeals
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing


@Composable
fun EmptyIngredientState(
    modifier: Modifier = Modifier,
    onAddItem: () -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val bodyColor    = MaterialTheme.colorScheme.onSurfaceVariant

    val annotated = buildAnnotatedString {
        withStyle(SpanStyle(color = bodyColor)) {
            append("Search and add foods to track your nutrition. Tap ")
        }
        pushStringAnnotation(tag = "ADD", annotation = "add")
        withStyle(
            SpanStyle(
                color = primaryColor,
                fontWeight = FontWeight.Bold
            )
        ) {
            append("+ Add Item")
        }
        pop()
        withStyle(SpanStyle(color = bodyColor)) {
            append(" to get started.")
        }
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = MaterialTheme.spacing.xl,
                    horizontal = MaterialTheme.spacing.lg
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.NoMeals,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    modifier = Modifier.size(36.dp)
                )
            }

            Text(
                text = "Your meal is empty",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            ClickableText(
                text = annotated,
                style = MaterialTheme.typography.bodySmall.copy(
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                ),
                onClick = { offset ->
                    annotated
                        .getStringAnnotations(tag = "ADD", start = offset, end = offset)
                        .firstOrNull()
                        ?.let { onAddItem() }
                }
            )
        }
    }
}

@Preview
@Composable
private fun OrevEmptyIngredientState() {
    ResponsiveAppTheme(
        darkTheme = true
    ) {
        EmptyIngredientState(
            onAddItem = {}
        )

    }
}