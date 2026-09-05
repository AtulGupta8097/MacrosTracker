package com.example.responsiveapp.presentation.settings.componet.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.Gender
import com.example.responsiveapp.presentation.commoncomponent.CustomButton
import com.example.responsiveapp.presentation.settings.model.ProfileSummaryUiState
import com.example.responsiveapp.presentation.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileSheet(
    sheetState: SheetState,
    profile: ProfileSummaryUiState,
    onSave: (name: String, heightCm: Float, age: Int, gender: Gender) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember(profile) { mutableStateOf(profile.name) }
    var height by remember(profile) { mutableStateOf(profile.rawHeight.toString()) }
    var age by remember(profile) { mutableStateOf(profile.rawAge.toString()) }
    var gender by remember(profile) { mutableStateOf(profile.gender) }

    val heightValue = height.toFloatOrNull()
    val ageValue = age.toIntOrNull()
    val isValid = name.isNotBlank() &&
            heightValue != null && heightValue > 0f &&
            ageValue != null && ageValue in 1..120

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = MaterialTheme.spacing.md)
        ) {
            Text(
                text = "Edit Profile",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = MaterialTheme.spacing.md),
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MaterialTheme.spacing.sm),
                label = { Text("Name") },
                singleLine = true,
            )

            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MaterialTheme.spacing.sm),
                label = { Text("Height (cm)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            )

            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MaterialTheme.spacing.md),
                label = { Text("Age") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            Text(
                text = "Gender",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.xs),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
            ) {
                Gender.entries.forEach { option ->
                    val isSelected = option == gender
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isSelected) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            )
                            .border(
                                width = 1.5.dp,
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                                },
                                shape = RoundedCornerShape(14.dp),
                            )
                            .clickable { gender = option }
                            .padding(vertical = MaterialTheme.spacing.sm),
                    ) {
                        Text(
                            text = option.label,
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }

            CustomButton(
                text = "Save Changes",
                enabled = isValid,
                onClick = {
                    if (heightValue != null && ageValue != null) {
                        onSave(name, heightValue, ageValue, gender)
                    }
                },
                buttonColors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.spacing.md),
            )
        }
    }
}
