package com.example.responsiveapp.presentation.myfood.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import com.example.responsiveapp.presentation.myfood.BasicInfoState
import com.example.responsiveapp.presentation.myfood.validation.BasicInfoErrors
import com.example.responsiveapp.presentation.myfood.validation.CreateFoodValidator
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

// CHANGED: rewritten. Pure display: no validation here, errors arrive via `errors`.
// The Next button moved to CreateFoodBottomActions.
@Composable
fun BasicInfoStep(
    state: BasicInfoState,
    errors: BasicInfoErrors,
    onFoodNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onServingSizeChanged: (String) -> Unit,
    onServingUnitChanged: (String) -> Unit,
    onServingsPerContainerChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(MaterialTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
    ) {
        FoodPreviewCard(
            name = state.foodName,
            description = state.description,
            servingText = CreateFoodValidator.combineServing(state.servingSize, state.servingUnit),
            servingsPerContainer = state.servingsPerContainer,
        )

        CustomFoodTextField(
            value = state.foodName,
            onValueChange = onFoodNameChanged,
            label = "Food Name",
            placeholder = "e.g. Protein Bar",
            icon = Icons.Outlined.Restaurant,
            error = errors.foodName,
            capitalization = KeyboardCapitalization.Words,
        )

        CustomFoodTextField(
            value = state.description,
            onValueChange = onDescriptionChanged,
            label = "Description (optional)",
            placeholder = "e.g. Crunchy oat bar with whey",
            icon = Icons.Outlined.Description,
            capitalization = KeyboardCapitalization.Sentences,
            singleLine = false,
            maxLines = 3,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
            CustomFoodTextField(
                value = state.servingSize,
                onValueChange = onServingSizeChanged,
                label = "Serving Size",
                placeholder = "100",
                icon = Icons.Outlined.Scale,
                error = errors.servingAmount,
                keyboardType = KeyboardType.Decimal,
                decimalOnly = true,
                modifier = Modifier.weight(1.2f),
            )

            CustomFoodTextField(
                value = state.servingUnit,
                onValueChange = onServingUnitChanged,
                label = "Unit",
                placeholder = "g, ml, cup",
                icon = Icons.Outlined.Straighten,
                error = errors.servingUnit,
                modifier = Modifier.weight(1f),
            )
        }

        CustomFoodTextField(
            value = state.servingsPerContainer,
            onValueChange = onServingsPerContainerChanged,
            label = "Servings Per Container",
            placeholder = "e.g. 6",
            icon = Icons.Outlined.Inventory2,
            error = errors.servingsPerContainer,
            keyboardType = KeyboardType.Decimal,
            decimalOnly = true,
            imeAction = ImeAction.Done,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBasicInfoStep() {
    ResponsiveAppTheme {
        BasicInfoStep(
            state = BasicInfoState(foodName = "Protein Bar", servingSize = "40", servingUnit = "g"),
            errors = BasicInfoErrors(servingsPerContainer = "Must be greater than 0"),
            onFoodNameChanged = {},
            onDescriptionChanged = {},
            onServingSizeChanged = {},
            onServingUnitChanged = {},
            onServingsPerContainerChanged = {},
        )
    }
}
