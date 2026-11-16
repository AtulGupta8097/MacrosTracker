package com.macrivo.app.presentation.foodbrowse.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
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
import com.macrivo.app.core.utils.formatMacroValue
import com.macrivo.app.domain.model.NutritionInfo
import com.macrivo.app.domain.model.Serving
import com.macrivo.app.presentation.commoncomponent.CustomButton
import com.macrivo.app.presentation.foodbrowse.FoodDetailMode
import com.macrivo.app.presentation.ui.theme.spacing

@Composable
fun FoodDetailContent(
    modifier: Modifier = Modifier,
    foodName: String,
    foodBrand: String? = null,
    foodServing: List<Serving>,
    nutritionLabel: String? = null,
    selectedServing: Serving,
    quantity: Float,
    scaledNutrition: NutritionInfo,
    isSaved: Boolean = false,
    onServingSelected: (Serving) -> Unit,
    onQuantityChanged: (Float) -> Unit,
    onBack: () -> Unit,
    onLog: () -> Unit,
    onSaveToggle: () -> Unit = {},
    mode: FoodDetailMode = FoodDetailMode.BROWSE,
    onDelete: () -> Unit = {},
    isBusy: Boolean = false,
    hasChanges: Boolean = false,
    onSaveChanges: () -> Unit = {},
) {
    val isBrowse = mode == FoodDetailMode.BROWSE
    val canEditQuantity = isBrowse || mode == FoodDetailMode.LOGGED_TODAY

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.lg),
    ) {
        item(key = "header") {
            FoodDetailHeader(
                foodName = foodName,
                foodBrand = foodBrand,
                selectedServing = selectedServing,
                nutrition = scaledNutrition,
                onBack = onBack,
                isSaved = isSaved,
                onSaveToggle = onSaveToggle,
                showSaveToggle = isBrowse,
                nutritionLabel = nutritionLabel,
            )
        }

        if (isBrowse && foodServing.size > 1) {
            item(key = "serving-selector") {
                ServingSelector(
                    servings = foodServing,
                    selectedServing = selectedServing,
                    onServingSelected = onServingSelected,
                )
            }
        }

        if (canEditQuantity) {
            item(key = "quantity-${quantity}") {
                QuantityControl(
                    quantity = quantity,
                    onQuantityChanged = { if (!isBusy) onQuantityChanged(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.md),
                )
            }
        } else {
            item(key = "quantity-readonly") {
                ReadOnlyQuantity(
                    quantity = quantity,
                    serving = selectedServing.description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.md),
                )
            }
        }

        item(key = "nutrition-label") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
            ) {
                Box(
                    Modifier
                        .width(3.dp)
                        .height(16.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(2.dp),
                        )
                )
                Text(
                    text = "NUTRITION FACTS",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                )
            }
        }

        item(key = "nutrition-table") {
            NutritionTable(
                nutrition = scaledNutrition,
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.md),
            )
        }

        when (mode) {
            FoodDetailMode.BROWSE -> item(key = "log-button") {
                CustomButton(
                    text = "Log Food",
                    onClick = onLog,
                    imageVector = Icons.Default.Add,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .height(80.dp)
                        .padding(MaterialTheme.spacing.md),
                    buttonColors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            }

            // Opened from Home, logged today: "Save changes" (after editing the quantity) and Delete.
            FoodDetailMode.LOGGED_TODAY -> item(key = "logged-today-actions") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(vertical = MaterialTheme.spacing.md),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
                ) {
                    CustomButton(
                        text = "Save changes",
                        onClick = onSaveChanges,
                        enabled = hasChanges && !isBusy,
                        imageVector = Icons.Default.Check,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 54.dp)
                            .padding(horizontal = MaterialTheme.spacing.sm),
                        buttonColors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    )
                    CustomButton(
                        text = "Delete",
                        onClick = onDelete,
                        enabled = !isBusy,
                        imageVector = Icons.Default.Delete,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 54.dp)
                            .padding(horizontal = MaterialTheme.spacing.sm
                            ),
                        buttonColors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                        ),
                    )
                }
            }

            // Opened from Home, logged on a previous day: no action at all.
            FoodDetailMode.LOGGED_PAST -> item(key = "view-only-note") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(MaterialTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        text = "Logged on a previous day. Past entries are view-only.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun ReadOnlyQuantity(
    quantity: Float,
    serving: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
        Text(
            text = "QUANTITY",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )
        Text(
            text = "${formatMacroValue(quantity)} \u00d7 $serving",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}