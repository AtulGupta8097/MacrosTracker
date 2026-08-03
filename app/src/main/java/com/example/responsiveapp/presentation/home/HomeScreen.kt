package com.example.responsiveapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.responsiveapp.presentation.home.componet.FoodLogsSection
import com.example.responsiveapp.presentation.home.componet.HealthMetricsSection
import com.example.responsiveapp.presentation.home.componet.HomeAppBar
import com.example.responsiveapp.presentation.home.componet.WeeklyDatePicker
import com.example.responsiveapp.presentation.home.componet.nutrition.NutritionProgressSection
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onEditProfile: () -> Unit = {},
    onSeeMoreProfile: () -> Unit = {},
    onLogFoodClick: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {

            HomeAppBar(
                appBarUiState = state.appBar,
                onEditProfile = onEditProfile,
                onSeeMoreProfile = onSeeMoreProfile
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(
                    vertical = MaterialTheme.spacing.md
                ),
                verticalArrangement = Arrangement.spacedBy(
                    MaterialTheme.spacing.md,
                ),
            ) {

                item {
                    WeeklyDatePicker(
                        modifier = Modifier.fillMaxWidth(),
                        datePickerUiState = state.datePicker,
                        onDateSelected = viewModel::onDateSelected,
                        onPreviousWeek = viewModel::onPreviousWeek,
                        onNextWeek = viewModel::onNextWeek,
                    )
                }

                item {
                    NutritionProgressSection(
                        modifier = Modifier.fillMaxWidth(),
                        nutritionUiState = state.nutrition,
                        onLogFoodClick = onLogFoodClick,
                    )
                }

                item {
                    FoodLogsSection(
                        recentMealsUiState = state.recentMeals,
                    )
                }

                item {
                    HealthMetricsSection(
                        healthMetricsUiState = state.healthMetrics,
                    )
                }
            }

        }

    }
}

@Preview
@Composable
private fun HomeScreenPrev() {
    ResponsiveAppTheme {
        HomeScreen(
            onEditProfile = {},
            onSeeMoreProfile = {},
            onLogFoodClick = {},

        )
    }
}