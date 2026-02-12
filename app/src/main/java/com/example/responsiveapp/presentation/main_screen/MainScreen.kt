package com.example.responsiveapp.presentation.main_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.responsiveapp.core.navigation.Routes
import com.example.responsiveapp.presentation.main_screen.component.*
import com.example.responsiveapp.presentation.ui.theme.DeviceConfiguration
import com.example.responsiveapp.presentation.ui.theme.deviceConfiguration

@Composable
fun MainScreen() {
    val deviceConfig = MaterialTheme.deviceConfiguration
    val navBackStack = rememberNavBackStack(Routes.HomeScreen)

    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomBarHeight by remember { mutableStateOf(0.dp) }

    val navItems =
        listOf(
            Navbar.Home,
            Navbar.Progress,
            Navbar.Goal,
            Navbar.Settings,
        )

    val selectedTabIndex =
        navItems
            .indexOfFirst {
                it.route == navBackStack.last()
            }.coerceAtLeast(0)

    when (deviceConfig) {
        DeviceConfiguration.MOBILE -> {
            Scaffold(
                bottomBar = {
                    BottomNav(
                        items = navItems,
                        selectedTab = selectedTabIndex,
                        onTabSelected = { index ->
                            navBackStack.navigateSingleTop(navItems[index].route)
                        },
                        onHeightMeasured = { height ->
                            bottomBarHeight = height
                        },
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { showBottomSheet = true },
                        modifier =
                            Modifier.offset(
                                y = (bottomBarHeight / 2) + 12.dp,
                            ),
                        shape = CircleShape,
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                },
                floatingActionButtonPosition = FabPosition.Center,
            ) { padding ->
                MainContent(
                    modifier = Modifier.padding(padding),
                    navBackStack = navBackStack,
                )
            }
        }

        DeviceConfiguration.TABLET,
        DeviceConfiguration.DESKTOP,
        -> {
            Scaffold { padding ->
                Row(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(padding),
                ) {
                    SideNav(
                        items = navItems,
                        selectedIndex = selectedTabIndex,
                        onItemSelected = { index ->
                            navBackStack.navigateSingleTop(navItems[index].route)
                        },
                        onAddClick = { showBottomSheet = true },
                    )

                    MainContent(
                        modifier = Modifier.weight(1f),
                        navBackStack = navBackStack,
                    )
                }
            }
        }
    }

    if (showBottomSheet) {
        AddFoodBottomSheet(
            onDismiss = { showBottomSheet = false },
            onOptionSelected = {},
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    navBackStack: NavBackStack<NavKey>,
) {
    val currentEntry = navBackStack.last()

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
    ) {
        AnimatedContent(
            targetState = currentEntry,
            transitionSpec = {
                slideInHorizontally { it } + fadeIn() togetherWith
                    slideOutHorizontally { -it } + fadeOut()
            },
            label = "NavTransition",
        ) { targetEntry ->

            androidx.compose.runtime.key(targetEntry) {
                NavDisplay(
                    entryDecorators =
                        listOf(
                            rememberSaveableStateHolderNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator(),
                        ),
                    backStack = navBackStack,
                    entryProvider =
                        entryProvider {
                            entry<Routes.HomeScreen> { HomeScreen() }
                            entry<Routes.ProgressScreen> { ProgressScreen() }
                            entry<Routes.GoalsScreen> { GoalScreen() }
                            entry<Routes.SettingsScreen> { SettingsScreen() }
                        },
                )
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Home Screen", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun ProgressScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Progress Screen", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun GoalScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Goal Screen", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun SettingsScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Settings Screen", modifier = Modifier.align(Alignment.Center))
    }
}
