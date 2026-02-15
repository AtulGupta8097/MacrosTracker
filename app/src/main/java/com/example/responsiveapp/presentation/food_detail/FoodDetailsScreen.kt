package com.example.responsiveapp.presentation.food_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.responsiveapp.domain.model.Serving

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailsScreen(
    userId: String,
    date: Long,
    onBackClick: () -> Unit,
    onLogSuccess: () -> Unit,
    viewModel: FoodDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    // Handle success
    LaunchedEffect(state.logSuccess) {
        if (state.logSuccess) {
            onLogSuccess()
        }
    }
    
    // Show error snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.onEvent(FoodDetailsEvent.DismissError)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Food Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                
                state.food != null -> {
                    FoodDetailsContent(
                        state = state,
                        onEvent = viewModel::onEvent,
                        userId = userId,
                        date = date
                    )
                }
                
                state.error != null -> {
                    ErrorContent(
                        error = state.error!!,
                        onRetry = { viewModel.onEvent(FoodDetailsEvent.RetryLoad) },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun FoodDetailsContent(
    state: FoodDetailsState,
    onEvent: (FoodDetailsEvent) -> Unit,
    userId: String,
    date: Long
) {
    val food = state.food ?: return
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Food Name & Brand
        Column {
            Text(
                text = food.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            if (food.brand != null) {
                Text(
                    text = food.brand,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Divider()
        
        // Serving Selection
        Text(
            text = "Select Serving",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        ServingSelector(
            servings = food.servings,
            selectedServing = state.selectedServing,
            onServingSelected = { 
                onEvent(FoodDetailsEvent.ServingSelected(it)) 
            }
        )
        
        Divider()
        
        // Quantity Input
        QuantityInput(
            quantity = state.quantity,
            onQuantityChanged = { 
                onEvent(FoodDetailsEvent.QuantityChanged(it)) 
            }
        )
        
        Divider()
        
        // Nutrition Info
        state.selectedServing?.let { serving ->
            NutritionInfoCard(
                serving = serving,
                quantity = state.quantity
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Log Button
        Button(
            onClick = { 
                onEvent(FoodDetailsEvent.LogFood(userId, date)) 
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = state.selectedServing != null && !state.isLoggingFood
        ) {
            if (state.isLoggingFood) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = "Log Food",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ServingSelector(
    servings: List<Serving>,
    selectedServing: Serving?,
    onServingSelected: (Serving) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedServing?.description ?: "Select serving",
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
        )
        
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            servings.forEach { serving ->
                DropdownMenuItem(
                    text = { 
                        Text("${serving.description} (${serving.amount}${serving.unit})") 
                    },
                    onClick = {
                        onServingSelected(serving)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun QuantityInput(
    quantity: Float,
    onQuantityChanged: (Float) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Quantity",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilledTonalButton(
                onClick = { 
                    if (quantity > 0.25f) onQuantityChanged(quantity - 0.25f) 
                }
            ) {
                Text("-")
            }
            
            Text(
                text = String.format("%.2f", quantity),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(80.dp)
            )
            
            FilledTonalButton(
                onClick = { onQuantityChanged(quantity + 0.25f) }
            ) {
                Text("+")
            }
        }
    }
}

@Composable
private fun NutritionInfoCard(
    serving: Serving,
    quantity: Float
) {
    val nutrition = serving.nutrition.calculateForQuantity(quantity)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Nutrition Facts",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Divider()
            
            NutritionRow(
                label = "Calories",
                value = "${nutrition.calories.toInt()} kcal",
                isLarge = true
            )
            
            Divider()
            
            NutritionRow(label = "Protein", value = "${nutrition.protein}g")
            NutritionRow(label = "Carbs", value = "${nutrition.carbs}g")
            NutritionRow(label = "Fat", value = "${nutrition.fat}g")
            
            if (nutrition.fiber > 0) {
                NutritionRow(label = "Fiber", value = "${nutrition.fiber}g")
            }
            
            if (nutrition.sugar > 0) {
                NutritionRow(label = "Sugar", value = "${nutrition.sugar}g")
            }
        }
    }
}

@Composable
private fun NutritionRow(
    label: String,
    value: String,
    isLarge: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isLarge) 
                MaterialTheme.typography.titleMedium 
            else 
                MaterialTheme.typography.bodyLarge,
            fontWeight = if (isLarge) FontWeight.Bold else FontWeight.Normal
        )
        
        Text(
            text = value,
            style = if (isLarge) 
                MaterialTheme.typography.titleMedium 
            else 
                MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
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
            style = MaterialTheme.typography.bodyMedium
        )
        
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}