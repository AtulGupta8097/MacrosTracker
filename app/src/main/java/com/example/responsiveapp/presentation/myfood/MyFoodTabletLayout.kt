package com.example.responsiveapp.presentation.myfood

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FoodBank
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.myfood.CustomFood
import com.example.responsiveapp.presentation.myfood.component.CreateFoodScreen

private val LIST_PANE_WIDTH = 300.dp

@Composable
fun MyFoodTabletLayout(
    state: MyFoodUIState,
    onCreateFoodClick: () -> Unit,
    onFoodCardClick: (CustomFood) -> Unit,
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit,
    onFoodNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onServingSizeChanged: (String) -> Unit,
    onServingsPerContainerChanged: (String) -> Unit,
    onCaloriesChanged: (String) -> Unit,
    onProteinChanged: (String) -> Unit,
    onCarbohydratesChanged: (String) -> Unit,
    onTotalFatChanged: (String) -> Unit,
    onFiberChanged: (String) -> Unit,
    onSugarChanged: (String) -> Unit,
    onSodiumChanged: (String) -> Unit,
    onCholesterolChanged: (String) -> Unit,
    onPotassiumChanged: (String) -> Unit,
    onCalciumChanged: (String) -> Unit,
    onIronChanged: (String) -> Unit,
    onVitaminAChanged: (String) -> Unit,
    onVitaminCChanged: (String) -> Unit,
    onVitaminDChanged: (String) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
) {

    val isOnList =
        state.destination is MyFoodDestination.List

    val isEditMode =
        state.destination is MyFoodDestination.Edit

    Row(
        modifier = Modifier.fillMaxSize()
    ) {

        MyFoodListScreen(
            modifier = Modifier
                .width(LIST_PANE_WIDTH)
                .fillMaxHeight(),
            foods = state.foods,
            selectedFoodId =
                state.editingFood?.id,
            onCreateFoodClick =
                onCreateFoodClick,
            onFoodCardClick =
                onFoodCardClick,
        )

        VerticalDivider(
            color =
                MaterialTheme
                    .colorScheme
                    .outlineVariant,
            thickness = 1.dp,
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        ) {

            if (isOnList) {

                RightPaneIdleHint()

            } else {
                CreateFoodScreen(
                    modifier =
                        Modifier.fillMaxSize(),
                    state = state,
                    onBack = {},

                    onNextStep =
                        onNextStep,

                    onFoodNameChanged =
                        onFoodNameChanged,
                    onDescriptionChanged =
                        onDescriptionChanged,
                    onServingSizeChanged =
                        onServingSizeChanged,
                    onServingsPerContainerChanged =
                        onServingsPerContainerChanged,

                    onCaloriesChanged =
                        onCaloriesChanged,
                    onProteinChanged =
                        onProteinChanged,
                    onCarbohydratesChanged =
                        onCarbohydratesChanged,
                    onTotalFatChanged =
                        onTotalFatChanged,
                    onFiberChanged =
                        onFiberChanged,
                    onSugarChanged =
                        onSugarChanged,

                    onSodiumChanged =
                        onSodiumChanged,
                    onCholesterolChanged =
                        onCholesterolChanged,
                    onPotassiumChanged =
                        onPotassiumChanged,
                    onCalciumChanged =
                        onCalciumChanged,
                    onIronChanged =
                        onIronChanged,

                    onVitaminAChanged =
                        onVitaminAChanged,
                    onVitaminCChanged =
                        onVitaminCChanged,
                    onVitaminDChanged =
                        onVitaminDChanged,

                    onSave =
                        onSave,
                    onDelete =
                        onDelete,
                )
            }
        }
    }
}

@Composable
private fun RightPaneIdleHint() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {

        Column(
            modifier =
                Modifier.padding(32.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.spacedBy(12.dp),
        ) {

            Icon(
                imageVector =
                    Icons.Outlined.FoodBank,
                contentDescription = null,
                tint = MaterialTheme
                    .colorScheme
                    .primary
                    .copy(alpha = 0.35f),
                modifier =
                    Modifier.size(64.dp),
            )

            Text(
                text =
                    "Select a food to edit\nor tap Create Custom Food",
                style =
                    MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                            .copy(alpha = 0.6f),
                        textAlign =
                            TextAlign.Center,
                    ),
            )
        }
    }
}