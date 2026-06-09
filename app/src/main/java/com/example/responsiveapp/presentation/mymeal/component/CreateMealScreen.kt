package com.example.responsiveapp.presentation.mymeal.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.responsiveapp.domain.model.MealIngredient
import com.example.responsiveapp.presentation.commoncomponent.CustomButton
import com.example.responsiveapp.presentation.commoncomponent.CustomInputField
import com.example.responsiveapp.presentation.commoncomponent.TopBar
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun CreateMealScreen(
    modifier: Modifier = Modifier,
    mealName: String,
    totalCal: Float,
    totalProtein: Float,
    totalCarbs: Float,
    totalFat: Float,
    ingredients: Map<String, MealIngredient>,
    hasUnsavedChanges: Boolean = false,
    onMealNameChanged: (String) -> Unit = {},
    onIngredientAdd: () -> Unit = {},
    isEditMode: Boolean = false,
    onSave: () -> Unit = {},
    onDelete: () -> Unit = {},
    onIngredientRemove: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    val saveEnabled = if (isEditMode) {
        hasUnsavedChanges && mealName.isNotBlank() && ingredients.isNotEmpty()
    } else {
        true // Always enable for create
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {

        TopBar(
            heading = if (isEditMode) "Edit Meal" else "Create Meal",
            onBack  = onBack,
            actions = {
                if (isEditMode) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete meal",
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            },
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(MaterialTheme.spacing.md)
        ) {

            Text(
                text = "Meal Name",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                modifier = Modifier.padding(start = MaterialTheme.spacing.sm)
            )

            CustomInputField(
                mealName = mealName,
                onMealNameChanged = onMealNameChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.sm),
                placeholder = "e.g. Post-Workout Bowl"
            )

            MacroSummarySection(
                modifier = Modifier.padding(top = MaterialTheme.spacing.md),
                totalCal = totalCal,
                totalProtein = totalProtein,
                totalCarbs = totalCarbs,
                totalFat = totalFat,
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = MaterialTheme.spacing.lg,
                        bottom = 0.dp,
                        start = MaterialTheme.spacing.sm,
                        end = MaterialTheme.spacing.sm
                    )
            ) {
                Text(
                    text = "MEAL ITEMS",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    ),
                )

                Surface(
                    onClick = onIngredientAdd,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs),
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add item",
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Add Item",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            AnimatedVisibility(visible = ingredients.isEmpty()) {
                EmptyIngredientState(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.lg),
                    onAddItem = onIngredientAdd
                )
            }

            AnimatedVisibility(
                visible = ingredients.isNotEmpty(),
                modifier = Modifier
                    .padding(top = MaterialTheme.spacing.lg)

            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
                ) {
                    ingredients.forEach { (key, ingredient) ->
                        IngredientCard(
                            ingredient = ingredient,
                            onRemove = { onIngredientRemove(key) }
                        )
                    }
                }
            }

        }
        CustomButton(
            text = if(isEditMode) "Edit Meal" else "Save Meal",
            onClick = onSave,
            enabled = saveEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp,max = 90.dp)
                .padding(
                    vertical = MaterialTheme.spacing.lg,
                    horizontal = MaterialTheme.spacing.md
                ),
            buttonColors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        )
    }
}

@Preview
@Composable
private fun PrevCreateMealScreen() {
    ResponsiveAppTheme(darkTheme = true) {
        CreateMealScreen(
            mealName = "mealName",
            onMealNameChanged = {},
            ingredients = mapOf(
                "id" to MealIngredient(
                    foodId = "id",
                    foodName = "Pizza"
                ),
                "id2" to MealIngredient(
                    foodId = "id2",
                    foodName = "Pizza2"
                ),
                "id3" to MealIngredient(
                    foodId = "id3",
                    foodName = "Pizza3"
                )
            ),
            totalCal = 100f,
            totalProtein = 100f,
            totalCarbs = 100f,
            totalFat = 38f,
            onIngredientAdd = {},
            onIngredientRemove = {}
        )
    }
}