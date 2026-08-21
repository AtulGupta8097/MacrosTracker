package com.example.responsiveapp.presentation.progress.componet.weight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun LogWeightDialog(
    currentWeight: Float? = null,
    onDismiss: () -> Unit,
    onConfirm: (Float) -> Unit,
) {
    var input by rememberSaveable {
        mutableStateOf("")
    }

    val parsedWeight = input.toFloatOrNull()

    val isValid =
        parsedWeight != null &&
                parsedWeight > 0f

    val accentColor =
        MaterialTheme.colorScheme.primary

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        ),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.spacing.lg,
                ),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            shadowElevation = 16.dp,
        ) {
            Column(
                modifier = Modifier.padding(
                    MaterialTheme.spacing.lg,
                ),
            ) {

                // Icon
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(64.dp)
                        .background(
                            color = accentColor.copy(
                                alpha = 0.14f,
                            ),
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector =
                            Icons.Outlined.MonitorWeight,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(30.dp),
                    )
                }

                // Title
                Text(
                    text = "Log Your Weight",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(
                            top = MaterialTheme.spacing.md,
                        ),
                    style =
                        MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color =
                        MaterialTheme.colorScheme.onSurface,
                )

                // Description
                Text(
                    text =
                        "Enter today's weight in kilograms",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(
                            top = MaterialTheme.spacing.xxs,
                        ),
                    style =
                        MaterialTheme.typography.bodyMedium,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant,
                )

                // Weight input
                OutlinedTextField(
                    value = input,

                    onValueChange = { newValue ->

                        if (
                            newValue.length <= 6 &&
                            newValue.matches(
                                Regex(
                                    "^\\d{0,3}(\\.\\d{0,2})?$",
                                ),
                            )
                        ) {
                            input = newValue
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = MaterialTheme.spacing.lg,
                        ),

                    placeholder = {
                        Text("e.g. 68.5")
                    },

                    suffix = {
                        Text(
                            text = "kg",
                            fontWeight =
                                FontWeight.Medium,
                        )
                    },

                    leadingIcon = {
                        Icon(
                            imageVector =
                                Icons.Outlined.MonitorWeight,
                            contentDescription = null,
                            tint = accentColor,
                        )
                    },

                    singleLine = true,

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Decimal,
                        ),

                    shape =
                        RoundedCornerShape(16.dp),

                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedBorderColor =
                                accentColor,

                            focusedLabelColor =
                                accentColor,

                            cursorColor =
                                accentColor,

                            focusedLeadingIconColor =
                                accentColor,

                            unfocusedLeadingIconColor =
                                MaterialTheme
                                    .colorScheme
                                    .onSurfaceVariant,
                        ),
                )

                // Current weight
                currentWeight?.let { weight ->

                    Box(
                        modifier = Modifier
                            .align(
                                Alignment.CenterHorizontally,
                            )
                            .padding(
                                top = MaterialTheme.spacing.md,
                            )
                            .background(
                                color = accentColor.copy(
                                    alpha = 0.10f,
                                ),
                                shape =
                                    RoundedCornerShape(50),
                            )
                            .padding(
                                horizontal = 16.dp,
                                vertical = 8.dp,
                            ),
                    ) {
                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Current: ",
                                style =
                                    MaterialTheme
                                        .typography
                                        .bodySmall,
                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .onSurfaceVariant,
                            )

                            Text(
                                text =
                                    "${"%.1f".format(weight)} kg",
                                style =
                                    MaterialTheme
                                        .typography
                                        .bodySmall,
                                fontWeight =
                                    FontWeight.Bold,
                                color = accentColor,
                            )
                        }
                    }
                }

                // Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = MaterialTheme.spacing.lg,
                        ),
                    horizontalArrangement =
                        Arrangement.spacedBy(
                            MaterialTheme.spacing.sm,
                        ),
                ) {

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier =
                            Modifier.weight(1f),
                        shape =
                            RoundedCornerShape(50),
                        colors =
                            ButtonDefaults
                                .outlinedButtonColors(
                                    contentColor =
                                        MaterialTheme
                                            .colorScheme
                                            .onSurfaceVariant,
                                ),
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            parsedWeight?.let(onConfirm)
                        },
                        modifier =
                            Modifier.weight(1f),
                        enabled = isValid,
                        shape =
                            RoundedCornerShape(50),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    accentColor,

                                contentColor =
                                    MaterialTheme
                                        .colorScheme
                                        .onPrimary,

                                disabledContainerColor =
                                    accentColor.copy(
                                        alpha = 0.25f,
                                    ),

                                disabledContentColor =
                                    MaterialTheme
                                        .colorScheme
                                        .onPrimary
                                        .copy(
                                            alpha = 0.55f,
                                        ),
                            ),
                    ) {
                        Text(
                            text = "Log Weight",
                            fontWeight =
                                FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }
}