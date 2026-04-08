package com.example.responsiveapp.presentation.food_browse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.responsiveapp.domain.model.FoodItem
import com.example.responsiveapp.presentation.food_browse.component.FoodCard
import com.example.responsiveapp.presentation.food_browse.component.SearchField
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun FoodBrowseListScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    data: List<FoodItem>,
    isVertical: Boolean,
    isLoading: Boolean,
    onFoodClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
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

        Box(Modifier.fillMaxSize()) {
            if(!isLoading) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .then(
                            if (isVertical) Modifier.navigationBarsPadding() else Modifier
                        ),
                    contentPadding = PaddingValues(
                        horizontal = MaterialTheme.spacing.md,
                        vertical = MaterialTheme.spacing.sm
                    ),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
                ) {
                    items(data, key = { it.id }) { food ->
                        FoodCard(
                            food = food,
                            onFoodClick = onFoodClick
                        )
                    }
                }
            }
            else {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}