package com.example.responsiveapp.presentation.myfood.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.responsiveapp.presentation.commoncomponent.CustomButton
import com.example.responsiveapp.presentation.commoncomponent.CustomInputField
import com.example.responsiveapp.presentation.myfood.BasicInfoState
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun BasicInfoStep(
    state: BasicInfoState,
    canProceedToNutrients: Boolean,
    onFoodNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onServingSizeChanged: (String) -> Unit,
    onServingsPerContainerChanged: (String) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.md)
            .verticalScroll(rememberScrollState())
    ) {
        FoodInfoHeaderCard(state = state)

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))

        FieldLabel("Food Name")

        CustomInputField(
            mealName = state.foodName,
            onMealNameChanged = onFoodNameChanged,
            placeholder = "e.g. Protein Bar, Granola",
            icon = Icons.Outlined.Restaurant,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))

        FieldLabel("Description")

        CustomInputField(
            mealName = state.description,
            onMealNameChanged = onDescriptionChanged,
            placeholder = "e.g. Crunchy oat bar with whey",
            icon = Icons.Outlined.Description,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))

        FieldLabel("Serving Size")

        CustomInputField(
            mealName = state.servingSize,
            onMealNameChanged = onServingSizeChanged,
            placeholder = "e.g. 1 bar (40g)",
            icon = Icons.Outlined.Scale,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))

        FieldLabel("Servings Per Container")

        CustomInputField(
            mealName = state.servingsPerContainer,
            onMealNameChanged = onServingsPerContainerChanged,
            placeholder = "e.g. 6",
            icon = Icons.Outlined.Inventory2,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(1f, fill = true))

        CustomButton(
            text = "Next",
            onClick = onNext,
            enabled = canProceedToNutrients,
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

@Composable
fun FoodInfoHeaderCard(
    state: BasicInfoState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
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

@Composable
fun FieldLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
        ),
        modifier = modifier
            .paddingFromBaseline(bottom = MaterialTheme.spacing.sm)
            .padding(start = MaterialTheme.spacing.sm),
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewBasicInfoStep() {
    ResponsiveAppTheme {
        BasicInfoStep(
            state = BasicInfoState(),
            canProceedToNutrients = true,
            onFoodNameChanged = {},
            onDescriptionChanged = {},
            onServingSizeChanged = {},
            onServingsPerContainerChanged = {},
            onNext = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}