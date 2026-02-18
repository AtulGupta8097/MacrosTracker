package com.example.responsiveapp.presentation.food_database_screen

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.food_database_screen.component.TopBar
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

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
    LaunchedEffect(pagerState.currentPage) {
        viewModel.onTabChanged(tabs[pagerState.currentPage])
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {

            TopBar(onBack = onBack)

            PrimaryScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary,
                edgePadding = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = tab.title,
                                maxLines = 1,
                                overflow = TextOverflow.Visible,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = if (pagerState.currentPage == index) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onBackground
                                    }
                                )
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (tabs[page]) {
                    FoodTab.ALL -> {
                        val query by  viewModel.searchQuery.collectAsState()
                        val allScreenState by viewModel.tab1State.collectAsState()
                        SearchFoodScreen(
                            query = query,
                            onQueryChange = {
                                viewModel.onQueryChange(it)
                            },
                            state = allScreenState

                        )
                    }
                    FoodTab.MY_MEALS -> { Text(tabs[page].title) }
                    FoodTab.MY_FOODS -> { Text(tabs[page].title) }
                    FoodTab.SAVED_SCANS -> { Text(tabs[page].title) }
                }
            }
        }
    }
}

