package com.example.responsiveapp.presentation.food_database_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.responsiveapp.domain.model.Food
import com.example.responsiveapp.presentation.food_database_screen.component.FoodCard
import com.example.responsiveapp.presentation.food_database_screen.component.SearchField
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun SearchFoodScreen(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    state: FoodDatabaseState<Food>,
    onFoodClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SearchField(
            query = query,
            onQueryChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.md)
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = MaterialTheme.spacing.md,
                    vertical = MaterialTheme.spacing.sm
                ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
            ) {
                items(state.data, key = { it.id }) { food ->
                    FoodCard(
                        food = food,
                        onFoodClick = onFoodClick
                    )
                }
            }
        }
    }
}