package com.example.responsiveapp.presentation.settings.componet.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.example.responsiveapp.presentation.commoncomponent.CustomButton
import com.example.responsiveapp.presentation.ui.theme.spacing

/**
 * Reused for both "Current Weight" and "Target Weight" - same shape of
 * editor (a single kg value), no reason for two near-identical sheets.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumericInputSheet(
    sheetState: SheetState,
    title: String,
    initialValue: Float,
    unit: String,
    onSave: (Float) -> Unit,
    onDismiss: () -> Unit,
) {
    var input by remember(initialValue) { mutableStateOf(initialValue.toString()) }

    val parsed = input.toFloatOrNull()
    val isValid = parsed != null && parsed > 0f && parsed < 500f

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
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = MaterialTheme.spacing.md),
            )

            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Weight ($unit)") },
                singleLine = true,
                isError = input.isNotEmpty() && !isValid,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            )

            CustomButton(
                text = "Save",
                enabled = isValid,
                onClick = { parsed?.let(onSave) },
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
