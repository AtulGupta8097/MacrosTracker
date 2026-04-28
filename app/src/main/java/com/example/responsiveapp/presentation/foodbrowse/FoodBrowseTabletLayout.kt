package com.example.responsiveapp.presentation.foodbrowse

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.Serving
import kotlinx.coroutines.flow.distinctUntilChanged

private val LIST_PANE_WIDTH = 300.dp

@Composable
fun FoodBrowseTabletLayout(
    state: FoodBrowseState,
    foodDetailUiState: FoodDetailUiState,
    onQueryChange: (String) -> Unit,
    onFoodClick: (String) -> Unit,
    onFirstVisibleFoodChanged: (String) -> Unit,
    onServingSelected: (Serving) -> Unit,
    onQuantityChanged: (Float) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState, state.foods) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { index ->
                state.foods.getOrNull(index)?.id?.let { foodId ->
                    onFirstVisibleFoodChanged(foodId)
                }
            }
    }

    Row(modifier = Modifier.fillMaxSize()) {
        FoodBrowseListScreen(
            query = state.query,
            onQueryChange = onQueryChange,
            data = state.foods,
            isLoading = state.isLoading,
            onFoodClick = onFoodClick,
            selectedFoodId = state.selectedFood?.id,
            listState = listState,
            modifier = Modifier
                .width(LIST_PANE_WIDTH)
                .fillMaxHeight(),
        )

        VerticalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp,
        )

        FoodDetailScreen(
            uiState = foodDetailUiState,
            onBack = {},
            onServingSelected = onServingSelected,
            onQuantityChanged = onQuantityChanged,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        )
    }
}