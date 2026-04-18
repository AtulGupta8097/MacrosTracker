package com.example.responsiveapp.presentation.food_browse.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.responsiveapp.domain.model.FoodDetail
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.presentation.ui.theme.CarbsColor
import com.example.responsiveapp.presentation.ui.theme.FatColor
import com.example.responsiveapp.presentation.ui.theme.ProteinColor
import com.example.responsiveapp.presentation.ui.theme.spacing
import kotlin.math.roundToInt

@Composable
fun FoodDetailHeader(
    foodName: String,
    foodBrand: String? = null,
    selectedServing: Serving,
    nutrition: NutritionInfo,
    onBack: () -> Unit
) {

    val inverseSurface = MaterialTheme.colorScheme.inverseSurface

    val headerBrush = remember(inverseSurface) {
        Brush.verticalGradient(
            listOf(
                inverseSurface,
                inverseSurface.copy(alpha = 0.92f),
            )
        )
    }

    val caloriesInt = remember(nutrition.calories) {
        nutrition.calories.roundToInt()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(headerBrush)
            .statusBarsPadding()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.sm, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.inverseOnSurface
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = foodName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    foodBrand?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.lg)
                    .padding(bottom = MaterialTheme.spacing.lg, top = MaterialTheme.spacing.sm),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.Bottom) {

                    AnimatedContent(
                        targetState = caloriesInt,
                        transitionSpec = {
                            (slideInVertically(
                                spring(stiffness = Spring.StiffnessMediumLow)
                            ) { -it } + fadeIn()) togetherWith fadeOut()
                        },
                        label = "CaloriesAnim"
                    ) { cal ->
                        Text(
                            text = "$cal",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.inverseOnSurface,
                                fontSize = 80.sp,
                                letterSpacing = (-2).sp
                            )
                        )
                    }

                    Text(
                        text = "  kcal",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier.padding(bottom = 14.dp)
                    )
                }

                Text(
                    text = "per ${selectedServing.description}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.45f),
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.lg)
    ) {
        Spacer(Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
        ) {
            MacroCard(
                label = "Protein",
                value = nutrition.protein,
                accentColor = ProteinColor,
                modifier = Modifier.weight(1f)
            )

            MacroCard(
                label = "Carbs",
                value = nutrition.carbs,
                accentColor = CarbsColor,
                modifier = Modifier.weight(1f)
            )

            MacroCard(
                label = "Fat",
                value = nutrition.fat,
                accentColor = FatColor,
                modifier = Modifier.weight(1f)
            )
        }
    }
}