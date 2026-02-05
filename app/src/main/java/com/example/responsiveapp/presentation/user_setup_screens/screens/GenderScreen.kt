package com.example.responsiveapp.presentation.user_setup_screens.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.Gender
import com.example.responsiveapp.presentation.ui.theme.spacing
import com.example.responsiveapp.presentation.user_setup_screens.component.OptionButton


@Composable
fun GenderScreen(
    selectedGender: Gender?,
    onGenderSelected: (Gender) -> Unit
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
                text = "What's your gender?",
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
                        text = "Male",
                        selected = selectedGender == Gender.MALE,
                        icon = Icons.Filled.Male,
                        onClick = { onGenderSelected(Gender.MALE) }
                    )
                    OptionButton(
                        text = "Female",
                        selected = selectedGender == Gender.FEMALE,
                        icon = Icons.Filled.Female,
                        onClick = { onGenderSelected(Gender.FEMALE) }
                    )

            }
        }
    }
}
