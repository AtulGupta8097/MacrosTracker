package com.example.responsiveapp.presentation.progress.componet.weight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.unit.dp
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

    val isValid = parsedWeight != null && parsedWeight > 0f

    val colorScheme = MaterialTheme.colorScheme
    val spacing = MaterialTheme.spacing
    val accentColor = colorScheme.primary

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        ),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.lg),
            shape = RoundedCornerShape(30.dp),
            color = colorScheme.surface,
            tonalElevation = 6.dp,
            shadowElevation = 20.dp,
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = spacing.lg,
                    vertical = spacing.xl,
                ),
            ) {

                // ---------------------------------------------------------
                // Hero
                // ---------------------------------------------------------

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(88.dp)
                        .background(
                            color = accentColor.copy(alpha = 0.06f),
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(66.dp)
                            .background(
                                color = accentColor.copy(alpha = 0.13f),
                                shape = CircleShape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MonitorWeight,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(32.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.size(spacing.lg))

                Text(
                    text = "Log your weight",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.size(spacing.xs))

                Text(
                    text = "Keep your progress up to date with today's measurement.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.85f),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.size(spacing.lg))

                // ---------------------------------------------------------
                // Weight input
                // ---------------------------------------------------------

                OutlinedTextField(
                    value = input,
                    onValueChange = { newValue ->
                        if (
                            newValue.length <= 6 &&
                            newValue.matches(
                                Regex("^\\d{0,3}(\\.\\d{0,2})?$")
                            )
                        ) {
                            input = newValue
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Weight")
                    },
                    placeholder = {
                        Text("68.5")
                    },
                    suffix = {
                        Text(
                            text = "kg",
                            fontWeight = FontWeight.Bold,
                            color = if (isValid) {
                                accentColor
                            } else {
                                colorScheme.onSurfaceVariant
                            },
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.MonitorWeight,
                            contentDescription = null,
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                    ),
                    shape = RoundedCornerShape(18.dp),
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        focusedLabelColor = accentColor,
                        cursorColor = accentColor,
                        focusedLeadingIconColor = accentColor,
                        unfocusedLeadingIconColor =
                            colorScheme.onSurfaceVariant,
                    ),
                )

                // ---------------------------------------------------------
                // Current weight context
                // ---------------------------------------------------------

                currentWeight?.let { weight ->

                    Spacer(modifier = Modifier.size(spacing.sm))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = colorScheme.surfaceVariant.copy(
                                    alpha = 0.55f
                                ),
                                shape = RoundedCornerShape(14.dp),
                            )
                            .padding(
                                horizontal = spacing.md,
                                vertical = spacing.sm,
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MonitorWeight,
                            contentDescription = null,
                            tint = colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp),
                        )

                        Spacer(modifier = Modifier.size(spacing.sm))

                        Text(
                            text = "Previous measurement",
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.onSurfaceVariant,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "${"%.1f".format(weight)} kg",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                            ),
                            color = colorScheme.onSurface,
                        )
                    }
                }

                Spacer(modifier = Modifier.size(spacing.xl))

                // ---------------------------------------------------------
                // Actions
                // ---------------------------------------------------------

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                ) {

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(0.8f),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = colorScheme.onSurfaceVariant,
                        ),
                    ) {
                        Text(
                            text = "Cancel",
                            fontWeight = FontWeight.SemiBold,
                        )
                    }

                    Button(
                        onClick = {
                            parsedWeight?.let(onConfirm)
                        },
                        modifier = Modifier.weight(1.2f),
                        enabled = isValid,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor,
                            contentColor = colorScheme.onPrimary,
                            disabledContainerColor =
                                accentColor.copy(alpha = 0.20f),
                            disabledContentColor =
                                colorScheme.onPrimary.copy(alpha = 0.55f),
                        ),
                    ) {
                        Text(
                            text = "Log Weight",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}