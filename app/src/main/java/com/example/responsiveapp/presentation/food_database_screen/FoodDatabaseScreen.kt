package com.example.responsiveapp.presentation.food_database_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.responsiveapp.presentation.food_database_screen.component.TopBar
import kotlinx.coroutines.launch

enum class FoodTab(val title: String) {
    ALL("All"),
    MY_MEALS("My meals"),
    MY_FOODS("My foods"),
    SAVED_SCANS("Saved scans")
}

@Composable
fun FoodDatabaseScreen(
    viewModel: FoodDatabaseViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val tabs = FoodTab.entries
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    val tab1Destination by viewModel.tab1Destination.collectAsState()
    val tab2Destination by viewModel.tab2Destination.collectAsState()

    val topBarVisible = tab1Destination == FoodDatabaseDestination.FoodList
            && tab2Destination == MyMealsDestination.MealList

    val isInSubScreen = tab1Destination != FoodDatabaseDestination.FoodList
            || tab2Destination != MyMealsDestination.MealList

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (topBarVisible) {
                TopBar(onBack = onBack)

                PrimaryScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    edgePadding = 0.dp,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.background
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch { pagerState.animateScrollToPage(index) }
                            },
                            text = {
                                Text(
                                    text = tab.title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Visible
                                )
                            }
                        )
                    }
                }
            }

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = !isInSubScreen,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (tabs[page]) {

                    FoodTab.ALL -> {
                        val query by viewModel.searchQuery.collectAsState()
                        val state by viewModel.tab1State.collectAsState()

                        BackHandler(enabled = tab1Destination != FoodDatabaseDestination.FoodList) {
                            viewModel.onBackFromFoodDetail()
                        }

                        when (val destination = tab1Destination) {
                            is FoodDatabaseDestination.FoodList -> {
                                SearchFoodScreen(
                                    query = query,
                                    onQueryChange = viewModel::onQueryChange,
                                    state = state,
                                    onFoodClick = viewModel::onFoodClick
                                )
                            }
                            is FoodDatabaseDestination.FoodDetail -> {
                                FoodDetailScreen(
                                    state = state,
                                    onBack = viewModel::onBackFromFoodDetail
                                )
                            }
                        }
                    }

                    FoodTab.MY_MEALS -> {
                        BackHandler(enabled = tab2Destination != MyMealsDestination.MealList) {
                            viewModel.onBackFromMeals()
                        }

                        when (val destination = tab2Destination) {
                            is MyMealsDestination.MealList -> {
                                MyMealsScreen(
                                    onMealClick = viewModel::onMealClick,
                                    onCreateMealClick = viewModel::onCreateMealClick
                                )
                            }
                            is MyMealsDestination.MealDetail -> {
                                MealDetailScreen(
                                    mealId = destination.mealId,
                                    onBack = viewModel::onBackFromMeals
                                )
                            }
                            is MyMealsDestination.CreateMeal -> {
                                CreateMealScreen(
                                    onBack = viewModel::onBackFromMeals
                                )
                            }
                        }
                    }

                    FoodTab.MY_FOODS -> Text("My foods")
                    FoodTab.SAVED_SCANS -> Text("Saved scans")
                }
            }
        }
    }
}