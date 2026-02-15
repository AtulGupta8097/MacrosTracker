package com.example.responsiveapp.presentation.food_searach

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.responsiveapp.domain.model.Food

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSearchScreen(
    onBackClick: () -> Unit,
    onFoodClick: (Food) -> Unit,
    viewModel: FoodSearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Food") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { 
                    viewModel.onEvent(FoodSearchEvent.SearchQueryChanged(it)) 
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search for food...") },
                leadingIcon = { 
                    Icon(Icons.Default.Search, contentDescription = "Search") 
                },
                singleLine = true
            )
            
            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    
                    state.error != null -> {
                        ErrorContent(
                            error = state.error!!,
                            onRetry = { 
                                viewModel.onEvent(FoodSearchEvent.RetrySearch) 
                            },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    
                    state.searchQuery.length < 2 -> {
                        Text(
                            text = "Enter at least 2 characters to search",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    state.searchResults.isEmpty() -> {
                        Text(
                            text = "No foods found for \"${state.searchQuery}\"",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    else -> {
                        FoodResultsList(
                            foods = state.searchResults,
                            onFoodClick = onFoodClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FoodResultsList(
    foods: List<Food>,
    onFoodClick: (Food) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(foods) { food ->
            FoodResultItem(
                food = food,
                onClick = { onFoodClick(food) }
            )
        }
    }
}

@Composable
private fun FoodResultItem(
    food: Food,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = food.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            if (food.brand != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = food.brand,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (food.isVerified) {
                    Text(
                        text = "✓ Verified",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                if (food.servings.isNotEmpty()) {
                    Text(
                        text = "${food.servings.size} serving${if (food.servings.size > 1) "s" else ""}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "⚠️ Error",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )
        
        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}