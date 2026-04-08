package com.example.responsiveapp.presentation.food_browse.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun ServingSelector(
    servings: List<Serving>,
    selectedServing: Serving,
    onServingSelected: (Serving) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
        Text(
            text = "SERVING SIZE",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold, letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.md)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
        ) {
            items(servings, key = { it.id }) { serving ->
                val isSelected = serving.id == selectedServing.id
                Surface(
                    modifier = Modifier.clip(RoundedCornerShape(50)).clickable { onServingSelected(serving) },
                    shape = RoundedCornerShape(50),
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    tonalElevation = if (isSelected) 0.dp else 2.dp,
                    shadowElevation = if (isSelected) 0.dp else 1.dp,
                ) {
                    Text(
                        text = serving.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
            }
        }
    }
}