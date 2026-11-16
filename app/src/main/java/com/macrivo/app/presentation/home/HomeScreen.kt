package com.macrivo.app.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.macrivo.app.presentation.commoncomponent.BannerAd
import com.macrivo.app.presentation.foodbrowse.FoodDetailScreen
import com.macrivo.app.presentation.foodbrowse.FoodDetailUiEvent
import com.macrivo.app.presentation.foodbrowse.FoodDetailUiState
import com.macrivo.app.presentation.home.componet.DailyGreeting
import com.macrivo.app.presentation.home.componet.FoodLogsSection
import com.macrivo.app.presentation.home.componet.HealthMetricsSection
import com.macrivo.app.presentation.home.componet.HomeAppBar
import com.macrivo.app.presentation.home.componet.WeeklyDatePicker
import com.macrivo.app.presentation.home.componet.nutrition.NutritionProgressSection
import com.macrivo.app.presentation.home.componet.skeleton.FoodLogsSectionSkeleton
import com.macrivo.app.presentation.home.componet.skeleton.HealthMetricsSectionSkeleton
import com.macrivo.app.presentation.home.componet.skeleton.NutritionSectionSkeleton
import com.macrivo.app.presentation.home.model.DateItemUiModel
import com.macrivo.app.presentation.home.model.DatePickerUiState
import com.macrivo.app.presentation.home.model.WeekUiState
import com.macrivo.app.presentation.ui.theme.ResponsiveAppTheme
import com.macrivo.app.presentation.ui.theme.spacing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onLogFoodClick: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val foodDetail by viewModel.foodDetail.collectAsStateWithLifecycle()

    val listSnackbar = remember { SnackbarHostState() }
    LaunchedEffect(viewModel) {
        viewModel.listMessages.collect { listSnackbar.showSnackbar(it) }
    }

    Box(modifier = modifier.fillMaxSize()) {
        HomeContent(
            modifier = Modifier.fillMaxSize(),
            state = state,
            listState = rememberLazyListState(),
            onLogFoodClick = onLogFoodClick,
            onFoodLogClick = viewModel::onFoodLogClick,
            onFoodLogDelete = viewModel::onDeleteFoodLogFromList,
            onDateSelected = viewModel::onDateSelected,
            onPreviousWeek = viewModel::onPreviousWeek,
            onNextWeek = viewModel::onNextWeek,
        )
        SnackbarHost(
            hostState = listSnackbar,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }

    foodDetail?.let { detail ->
        FoodDetailSheet(
            detail = detail,
            messages = viewModel.messages,
            onDismiss = viewModel::onFoodDetailDismiss,
            onDelete = viewModel::onDeleteFoodLog,
            onQuantityChanged = viewModel::onQuantityChanged,
            onSaveChanges = viewModel::onSaveFoodLogChanges,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FoodDetailSheet(
    detail: FoodDetailUiState,
    messages: Flow<String>,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onQuantityChanged: (Float) -> Unit,
    onSaveChanges: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(messages) {
        messages.collect { snackbarHostState.showSnackbar(it) }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        FoodDetailScreen(
            uiState = detail,
            onEvent = { event ->
                when (event) {
                    FoodDetailUiEvent.Back ->
                        scope.launch { sheetState.hide() }.invokeOnCompletion { onDismiss() }
                    FoodDetailUiEvent.DeleteConfirmed -> onDelete()
                    is FoodDetailUiEvent.QuantityChanged -> onQuantityChanged(event.quantity)
                    FoodDetailUiEvent.SaveChangesClicked -> onSaveChanges()
                    is FoodDetailUiEvent.ServingSelected,
                    FoodDetailUiEvent.SaveToggled,
                    FoodDetailUiEvent.LogClicked -> Unit
                }
            },
            snackbarHostState = snackbarHostState,
        )
    }
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    onLogFoodClick: () -> Unit,
    onFoodLogClick: (String) -> Unit,
    onFoodLogDelete: (String) -> Unit = {},
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
                            onMealClick = onFoodLogClick,
                            onMealDelete = onFoodLogDelete,
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
            onFoodLogClick = {},
            onDateSelected = {},
            onPreviousWeek = {},
            onNextWeek = {},
        )
    }
}
