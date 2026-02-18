package com.example.responsiveapp.presentation.food_database_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
    query: String,
    onQueryChange: (String) -> Unit,
    state: FoodDatabaseState<Food>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SearchField(
                query = query,
                onQueryChange = {
                    onQueryChange(it)
                },
                modifier = modifier.fillMaxWidth()
                    .padding(MaterialTheme.spacing.md)
            )
            AnimatedVisibility(state.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

            }
            if(!state.isLoading) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    items(
                        state.data,
                        key = {
                            it.id
                        }
                    ) {
                        FoodCard(it)
                    }
                }
            }

        }
    }
}

