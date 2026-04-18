package com.example.responsiveapp.presentation.food_browse.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.presentation.common_component.CustomButton
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun FoodDetailContent(
    modifier: Modifier = Modifier,
    foodName: String,
    foodBrand: String? = null,
    foodServing: List<Serving>,
    selectedServing: Serving,
    quantity: Float,
    scaledNutrition: NutritionInfo,
    onServingSelected: (Serving) -> Unit,
    onQuantityChanged: (Float) -> Unit,
    onBack: () -> Unit,
    onLog: () -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.lg),
    ) {
        item {
            FoodDetailHeader(
                foodName = foodName,
                foodBrand = foodBrand,
                selectedServing = selectedServing,
                nutrition = scaledNutrition,
                onBack = onBack,
            )
        }

        if (foodServing.size > 1) {
            item {
                ServingSelector(
                    servings = foodServing,
                    selectedServing = selectedServing,
                    onServingSelected = onServingSelected,
                )
            }
        }

        item {
            QuantityControl(
                quantity = quantity,
                onQuantityChanged = onQuantityChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.md),
            )
        }

        item {
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

        item {
            NutritionTable(
                nutrition = scaledNutrition,
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.md),
            )
        }

        item {
            CustomButton(
                text = "Log Food",
                onClick = onLog,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(MaterialTheme.spacing.md),
                buttonColors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        }
    }
}