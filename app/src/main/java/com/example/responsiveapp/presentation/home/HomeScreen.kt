package com.example.responsiveapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.responsiveapp.presentation.commoncomponent.BannerAd
import com.example.responsiveapp.presentation.home.componet.DailyGreeting
import com.example.responsiveapp.presentation.home.componet.FoodLogsSection
import com.example.responsiveapp.presentation.home.componet.HealthMetricsSection
import com.example.responsiveapp.presentation.home.componet.HomeAppBar
import com.example.responsiveapp.presentation.home.componet.WeeklyDatePicker
import com.example.responsiveapp.presentation.home.componet.nutrition.NutritionProgressSection
import com.example.responsiveapp.presentation.home.componet.skeleton.FoodLogsSectionSkeleton
import com.example.responsiveapp.presentation.home.componet.skeleton.HealthMetricsSectionSkeleton
import com.example.responsiveapp.presentation.home.componet.skeleton.NutritionSectionSkeleton
import com.example.responsiveapp.presentation.home.model.DateItemUiModel
import com.example.responsiveapp.presentation.home.model.DatePickerUiState
import com.example.responsiveapp.presentation.home.model.WeekUiState
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onLogFoodClick: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeContent(
        modifier = modifier,
        state = state,
        listState = rememberLazyListState(),
        onLogFoodClick = onLogFoodClick,
        onDateSelected = viewModel::onDateSelected,
        onPreviousWeek = viewModel::onPreviousWeek,
        onNextWeek = viewModel::onNextWeek,
    )
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    onLogFoodClick: () -> Unit,
    onDateSelected: (Long) -> Unit,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
) {
    val spacing = MaterialTheme.spacing

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {

            HomeAppBar(
                streak = state.streak,
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(vertical = spacing.md),
                verticalArrangement = Arrangement.spacedBy(spacing.md),
            ) {

                item(key = "greeting"){
                    DailyGreeting(
                        modifier = Modifier.fillMaxWidth(),
                        name = state.userName,
                    )
                }

                if (!state.isLoading) {
                    item {
                        BannerAd(modifier = Modifier.fillMaxWidth())
                    }
                }

                item(key = "datePicker") {
                    WeeklyDatePicker(
                        modifier = Modifier.fillMaxWidth(),
                        datePickerUiState = state.datePicker,
                        onDateSelected = onDateSelected,
                        onPreviousWeek = onPreviousWeek,
                        onNextWeek = onNextWeek,
                    )
                }

                item(key = "nutrition") {
                    LoadingContent(
                        isLoading = state.isLoading,
                        skeleton = {
                            NutritionSectionSkeleton()
                        },
                    ) {
                        NutritionProgressSection(
                            modifier = Modifier.fillMaxWidth(),
                            nutritionUiState = state.nutrition,
                            onLogFoodClick = onLogFoodClick,
                        )
                    }
                }

                item(key = "foodLogs") {
                    LoadingContent(
                        isLoading = state.isLoading,
                        skeleton = {
                            FoodLogsSectionSkeleton()
                        },
                    ) {
                        FoodLogsSection(
                            recentMealsUiState = state.recentMeals,
                        )
                    }
                }

                item(key = "healthMetrics") {
                    LoadingContent(
                        isLoading = state.isLoading,
                        skeleton = {
                            HealthMetricsSectionSkeleton()
                        },
                    ) {
                        HealthMetricsSection(
                            healthMetricsUiState = state.healthMetrics,
                        )
                    }
                }

                if (!state.isLoading) {
                    item {
                        BannerAd(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

@Composable
private inline fun LoadingContent(
    isLoading: Boolean,
    skeleton: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    if (isLoading) {
        skeleton()
    } else {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentPreview() {
    ResponsiveAppTheme {
        HomeContent(
            state = HomeUiState(
                isLoading = false,
                datePicker = DatePickerUiState(
                    week = WeekUiState(
                        weekStartDate = 0L,
                        days = listOf(
                            DateItemUiModel(
                                epochMillis = 0L,
                                weekdayLabel = "Sun",
                                dayLabel = "1",
                                isSelected = false,
                                isToday = false,
                                isFuture = false,
                            ),
                            DateItemUiModel(
                                epochMillis = 0L,
                                weekdayLabel = "Mon",
                                dayLabel = "2",
                                isSelected = false,
                                isToday = false,
                                isFuture = false,
                            ),
                            DateItemUiModel(
                                epochMillis = 0L,
                                weekdayLabel = "Tue",
                                dayLabel = "3",
                                isSelected = true,
                                isToday = true,
                                isFuture = false,
                            ),
                            DateItemUiModel(
                                epochMillis = 0L,
                                weekdayLabel = "Wed",
                                dayLabel = "4",
                                isSelected = false,
                                isToday = false,
                                isFuture = true,
                            ),
                            DateItemUiModel(
                                epochMillis = 0L,
                                weekdayLabel = "Thu",
                                dayLabel = "5",
                                isSelected = false,
                                isToday = false,
                                isFuture = true,
                            ),
                            DateItemUiModel(
                                epochMillis = 0L,
                                weekdayLabel = "Fri",
                                dayLabel = "6",
                                isSelected = false,
                                isToday = false,
                                isFuture = true,
                            ),
                            DateItemUiModel(
                                epochMillis = 0L,
                                weekdayLabel = "Sat",
                                dayLabel = "7",
                                isSelected = false,
                                isToday = false,
                                isFuture = true,
                            )
                        ),
                    )
                )
            ),
            listState = rememberLazyListState(),
            onLogFoodClick = {},
            onDateSelected = {},
            onPreviousWeek = {},
            onNextWeek = {},
        )
    }
}
