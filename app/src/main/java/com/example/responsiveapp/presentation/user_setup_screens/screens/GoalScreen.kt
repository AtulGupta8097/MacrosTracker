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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.Goal
import com.example.responsiveapp.presentation.ui.theme.spacing
import com.example.responsiveapp.presentation.user_setup_screens.component.OptionButton

@Composable
fun GoalScreen(
    selectedGoal: Goal?,
    onGoalSelected: (Goal) -> Unit
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
                text = "What's your goal?",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Text(
                text = "It will help us calculate your macros",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(MaterialTheme.spacing.sm,
                    top = MaterialTheme.spacing.md, bottom = MaterialTheme.spacing.xxl)
            )


            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OptionButton(
                    text = Goal.MAINTAIN.label,
                    description = Goal.MAINTAIN.description,
                    icon = Icons.Filled.Balance,
                    selected = selectedGoal == Goal.MAINTAIN,
                    onClick = { onGoalSelected(Goal.MAINTAIN) },
                )
                OptionButton(
                    text = Goal.GAIN.label,
                    description = Goal.GAIN.description,
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    selected = selectedGoal == Goal.GAIN,
                    onClick = { onGoalSelected(Goal.GAIN) },
                )
                OptionButton(
                    text = Goal.LOSE.label,
                    description = Goal.LOSE.description,
                    icon = Icons.Filled.LocalFireDepartment,
                    selected = selectedGoal == Goal.LOSE,
                    onClick = { onGoalSelected(Goal.LOSE) },
                )

            }
        }
    }
}