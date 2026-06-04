package com.example.responsiveapp.presentation.myfood.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.responsiveapp.presentation.commoncomponent.CustomButton
import com.example.responsiveapp.presentation.commoncomponent.CustomInputField
import com.example.responsiveapp.presentation.myfood.MyFoodUIState
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun BasicInfoStep(
    state: MyFoodUIState,
    onFoodNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onServingSizeChanged: (String) -> Unit,
    onServingsPerContainerChanged: (String) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(MaterialTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        shape = MaterialTheme.shapes.large,
                    )
                    .padding(MaterialTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Fastfood,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp),
                    )

                    Text(
                        text = state.foodName.ifBlank { "Food name" },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (state.foodName.isBlank()) {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                        ),
                    )
                }

                if (state.description.isNotBlank()) {
                    Text(
                        text = state.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                if (state.servingSize.isNotBlank()) {
                    Text(
                        text = "Serving: ${state.servingSize}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }

        item {
            FieldLabel("Food Name")
        }

        item {
            CustomInputField(
                mealName = state.foodName,
                onMealNameChanged = onFoodNameChanged,
                placeholder = "e.g. Protein Bar, Granola",
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item {
            FieldLabel("Description")
        }

        item {
            CustomInputField(
                mealName = state.description,
                onMealNameChanged = onDescriptionChanged,
                placeholder = "e.g. Crunchy oat bar with whey",
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item {
            FieldLabel("Serving Size")
        }

        item {
            CustomInputField(
                mealName = state.servingSize,
                onMealNameChanged = onServingSizeChanged,
                placeholder = "e.g. 1 bar (40g)",
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item {
            FieldLabel("Servings Per Container")
        }

        item {
            CustomInputField(
                mealName = state.servingsPerContainer,
                onMealNameChanged = onServingsPerContainerChanged,
                placeholder = "e.g. 6",
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item {
            Spacer(
                modifier = Modifier.height(MaterialTheme.spacing.sm),
            )

            CustomButton(
                text = "Next",
                onClick = onNext,
                enabled = state.canProceedToNutrients,
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                buttonColors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                ),
            )
        }
    }
}

@Composable
fun FieldLabel(
    text: String,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
        ),
        modifier = Modifier.padding(
            start = MaterialTheme.spacing.sm,
        ),
    )
}