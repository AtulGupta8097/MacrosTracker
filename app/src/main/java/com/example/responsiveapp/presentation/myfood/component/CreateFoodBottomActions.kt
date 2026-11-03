package com.example.responsiveapp.presentation.myfood.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.commoncomponent.CustomButton
import com.example.responsiveapp.presentation.myfood.CreateFoodStep
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

// CHANGED: new file. Always-visible action bar pinned under the scrolling form, so Save is
// reachable on small screens. Buttons stay enabled (tapping an invalid form shows inline
// errors instead of a dead, greyed-out button); Save is locked only while a save runs.
@Composable
fun CreateFoodBottomActions(
    currentStep: CreateFoodStep,
    isEditMode: Boolean,
    isSaving: Boolean,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val primaryColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
    )

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        when (currentStep) {
            CreateFoodStep.BASIC_INFO -> {
                CustomButton(
                    text = "Next",
                    onClick = onNext,
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    buttonColors = primaryColors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.md)
                        .height(56.dp),
                )
            }

            CreateFoodStep.NUTRIENTS -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.md),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        enabled = !isSaving,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                    ) {
                        Text(
                            text = "Back",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        )
                    }

                    CustomButton(
                        text = if (isEditMode) "Update Food" else "Create Food",
                        onClick = onSave,
                        isLoading = isSaving,
                        enabled = !isSaving,
                        buttonColors = primaryColors,
                        modifier = Modifier
                            .weight(2f)
                            .height(56.dp),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBottomActionsNutrients() {
    ResponsiveAppTheme {
        CreateFoodBottomActions(
            currentStep = CreateFoodStep.NUTRIENTS,
            isEditMode = false,
            isSaving = false,
            onNext = {},
            onBack = {},
            onSave = {},
        )
    }
}
