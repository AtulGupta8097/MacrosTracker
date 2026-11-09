package com.macrivo.app.presentation.foodbrowse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import com.macrivo.app.domain.model.FoodItem
import com.macrivo.app.presentation.foodbrowse.component.FoodCard
import com.macrivo.app.presentation.foodbrowse.component.SearchField
import com.macrivo.app.presentation.ui.theme.spacing

@Composable
fun FoodBrowseListScreen(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    data: List<FoodItem>,
    isLoading: Boolean,
    onFoodClick: (String) -> Unit,
    selectedFoodId: String? = null,
    error: String? = null,
    listState: LazyListState = rememberLazyListState(),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        SearchField(
            query = query,
            onQueryChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.md),
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when {
                data.isEmpty() && isLoading -> {
                    CircularProgressIndicator()
                }

                data.isEmpty() && error != null -> {
                    SearchMessage(
                        title = "Couldn't load foods",
                        message = error,
                        isError = true,
                    )
                }

                data.isEmpty() -> {
                    val emptyMessage = if (query.isBlank()) {
                        "No foods to show right now. Try searching for a food."
                    } else {
                        "No foods found for \"${query.trim()}\". Try another search."
                    }
                    SearchMessage(
                        title = "No foods found",
                        message = emptyMessage,
                    )
                }

                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding(),
                        contentPadding = PaddingValues(
                            horizontal = MaterialTheme.spacing.md,
                            vertical = MaterialTheme.spacing.sm,
                        ),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
                    ) {
                        if (isLoading || error != null) {
                            item(contentType = "search_status") {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs),
                                ) {
                                    if (isLoading) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = MaterialTheme.spacing.xs),
                                        ) {
                                            CircularProgressIndicator(Modifier.size(18.dp))
                                            Text(
                                                text = "Refreshing foods…",
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                style = MaterialTheme.typography.bodySmall,
                                            )
                                        }
                                    }
                                    if (error != null) {
                                        Text(
                                            text = error,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                    }
                                }
                            }
                        }

                        items(
                            items = data,
                            key = { it.id },
                            contentType = { "food_item" },
                        ) { food ->
                            FoodCard(
                                food = food,
                                onFoodClick = { onFoodClick(food.id) },
                                isSelected = food.id == selectedFoodId,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchMessage(
    title: String,
    message: String,
    isError: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Text(
            text = message,
            color = if (isError) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
    }
}
