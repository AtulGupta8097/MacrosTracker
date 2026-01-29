package com.example.responsiveapp.presentation.user_setup_screens.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.ActivityLevel
import com.example.responsiveapp.presentation.ui.theme.spacing
import com.example.responsiveapp.presentation.user_setup_screens.component.OptionButton

@Composable
fun ActivityLevelScreen(
    selectedActivityLevel: ActivityLevel?,
    onActivityLevelSelected: (ActivityLevel) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(MaterialTheme.spacing.md)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))

            Text(
                text = "How Active are you?",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Text(
                text = "Help us personalize your experience",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(MaterialTheme.spacing.sm,
                    top = MaterialTheme.spacing.md, bottom = MaterialTheme.spacing.xxl)
            )


            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OptionButton(
                    text = ActivityLevel.LIGHT.label,
                    description = ActivityLevel.LIGHT.description,
                    selected = selectedActivityLevel == ActivityLevel.LIGHT,
                    onClick = { onActivityLevelSelected(ActivityLevel.LIGHT) }
                )
                OptionButton(
                    text = ActivityLevel.MODERATE.label,
                    description = ActivityLevel.MODERATE.description,
                    selected = selectedActivityLevel == ActivityLevel.MODERATE,
                    onClick = { onActivityLevelSelected(ActivityLevel.MODERATE) }
                )
                OptionButton(
                    text = ActivityLevel.ACTIVE.label,
                    description = ActivityLevel.ACTIVE.description,
                    selected = selectedActivityLevel == ActivityLevel.ACTIVE,
                    onClick = { onActivityLevelSelected(ActivityLevel.ACTIVE) }
                )
                OptionButton(
                    text = ActivityLevel.VERY_ACTIVE.label,
                    description = ActivityLevel.VERY_ACTIVE.description,
                    selected = selectedActivityLevel == ActivityLevel.VERY_ACTIVE,
                    onClick = { onActivityLevelSelected(ActivityLevel.VERY_ACTIVE) }
                )

            }
        }
    }
}