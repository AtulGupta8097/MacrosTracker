package com.example.responsiveapp.presentation.home.componet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.responsiveapp.presentation.home.model.DateItemUiModel

@Composable
fun WeeklyDateRow(
    days: List<DateItemUiModel>,
    dimensions: DatePickerDimensions,
    onDateSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(
                dimensions.itemSpacing
            ),
        ) {

            days.forEach { day ->

                DateItem(
                    day = day,
                    dimensions = dimensions,
                    onClick = onDateSelected,
                )
            }
        }
    }
}