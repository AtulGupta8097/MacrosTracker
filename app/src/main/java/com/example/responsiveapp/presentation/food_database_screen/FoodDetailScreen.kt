package com.example.responsiveapp.presentation.food_database_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.responsiveapp.domain.model.Food

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen(
    state: FoodDatabaseState<Food>,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = state.foodDetailData?.name ?: "")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.foodDetailLoading -> CircularProgressIndicator()
                state.foodDetailError != null -> Text(text = state.foodDetailError)
                state.foodDetailData != null -> {
                    // Replace with your real detail content
                    Text(text = state.foodDetailData.name)
                }
            }
        }
    }
}