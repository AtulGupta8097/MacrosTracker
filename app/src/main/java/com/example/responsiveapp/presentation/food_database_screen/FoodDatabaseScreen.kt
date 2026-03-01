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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.food_browse.FoodBrowse
import com.example.responsiveapp.presentation.food_database_screen.component.TopBar
import com.example.responsiveapp.presentation.ui.theme.grey
import kotlinx.coroutines.launch

enum class FoodTab(val title: String) {
    ALL("All"),
    MY_MEALS("My meals"),
    MY_FOODS("My foods"),
    SAVED_SCANS("Saved scans")
}

@Composable
fun FoodDatabaseScreen(
    onBack: () -> Unit = {}
) {
    val tabs = FoodTab.entries
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    var isAtRoot by remember { mutableStateOf(true) }

    LaunchedEffect(pagerState.currentPage) {
        isAtRoot = true
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

            if (isAtRoot) {
                TopBar(onBack = onBack)

                PrimaryScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    edgePadding = 0.dp,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.background
                ) {
                    tabs.forEachIndexed { index, tab ->
                        val selected = pagerState.currentPage == index

                        Tab(
                            selected = selected,
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
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        color = if (selected)
                                            MaterialTheme.colorScheme.primary
                                        else grey,
                                        fontWeight = if (selected)
                                            FontWeight.Normal
                                        else FontWeight.Thin
                                    )
                                )
                            }
                        )
                    }
                }
            }

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = isAtRoot,
                modifier = Modifier.fillMaxSize()
            ) { page ->

                when (tabs[page]) {

                    FoodTab.ALL ->  {
                        FoodBrowse (
                            onRootChanged = { isAtRoot = it }
                        )
                    }

                    FoodTab.MY_MEALS -> {}

                    FoodTab.MY_FOODS -> {}

                    FoodTab.SAVED_SCANS -> {}
                }
            }
        }
    }
}