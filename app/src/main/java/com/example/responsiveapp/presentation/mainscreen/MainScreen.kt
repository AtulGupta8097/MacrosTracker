package com.example.responsiveapp.presentation.mainscreen

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.responsiveapp.core.navigation.Routes
import com.example.responsiveapp.presentation.barcode.BarcodeScanScreen
import com.example.responsiveapp.presentation.fooddatabase.FoodDatabaseScreen
import com.example.responsiveapp.presentation.home.HomeScreen
import com.example.responsiveapp.presentation.mainscreen.component.AddFoodBottomSheet
import com.example.responsiveapp.presentation.mainscreen.component.BottomNav
import com.example.responsiveapp.presentation.mainscreen.component.Navbar
import com.example.responsiveapp.presentation.mainscreen.component.SideNav
import com.example.responsiveapp.presentation.progress.ProgressScreen
import com.example.responsiveapp.presentation.settings.SettingsScreen
import com.example.responsiveapp.presentation.ui.theme.DeviceConfiguration
import com.example.responsiveapp.presentation.ui.theme.deviceConfiguration

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun MainScreen(
    onSignedOut: () -> Unit,
    onAccountDeleted: () -> Unit
) {

    val deviceConfig = MaterialTheme.deviceConfiguration
    val navBackStack = rememberNavBackStack(Routes.HomeScreen)

    var showBottomSheet by remember { mutableStateOf(false) }

    val currentRoute = navBackStack.lastOrNull()

    val routesWithBottomBar = listOf(
        Routes.HomeScreen,
        Routes.ProgressScreen,
        Routes.SettingsScreen
    )

    val showBottomBar = currentRoute in routesWithBottomBar

    val navItems = listOf(
        Navbar.Home,
        Navbar.Progress,
        Navbar.Settings,
    )

    val selectedTabIndex =
        navItems.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)

    when (deviceConfig) {

        DeviceConfiguration.MOBILE -> {

            if (showBottomBar) {
                Scaffold(
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    bottomBar = {
                        BottomNav(
                            items = navItems,
                            selectedTab = selectedTabIndex,
                            onTabSelected = { index ->
                                navBackStack.navigateSingleTop(navItems[index].route)
                            },
                            onAddClick = { showBottomSheet = true },
                        )
                    },
                ) { padding ->
                    MainContent(
                        modifier = Modifier.padding(padding),
                        navBackStack = navBackStack,
                        onSignedOut = onSignedOut,
                        onAccountDeleted = onAccountDeleted,
                    )
                }
            } else {
                MainContent(
                    modifier = Modifier.fillMaxSize(),
                    navBackStack = navBackStack,
                    onSignedOut = onSignedOut,
                    onAccountDeleted = onAccountDeleted,
                )
            }
        }

        DeviceConfiguration.TABLET,
        DeviceConfiguration.DESKTOP -> {

            Scaffold(
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
            ) { padding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {

                    if (showBottomBar) {
                        SideNav(
                            items = navItems,
                            selectedIndex = selectedTabIndex,
                            onItemSelected = { index ->
                                navBackStack.navigateSingleTop(navItems[index].route)
                            },
                            onAddClick = { showBottomSheet = true },
                        )
                    }

                    MainContent(
                        modifier = Modifier.weight(1f),
                        navBackStack = navBackStack,
                        onSignedOut = onSignedOut,
                        onAccountDeleted = onAccountDeleted,
                    )
                }
            }
        }
    }

    if (showBottomSheet) {
        AddFoodBottomSheet(
            onDismiss = { showBottomSheet = false },
            onOptionSelected = { option ->
                if (option == "Food Database") {
                    navBackStack.navigateSingleTop(Routes.FoodDatabaseScreen)
                }
                else if (option == "Scan Barcode") {
                    navBackStack.navigateSingleTop(Routes.BarcodeScanScreen)
                }
            },
        )
    }
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    navBackStack: NavBackStack<NavKey>,
    onSignedOut: () -> Unit,
    onAccountDeleted: () -> Unit,
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {

        NavDisplay(
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            backStack = navBackStack,
            transitionSpec = {
                slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it } + fadeOut()
            },
            entryProvider = entryProvider {

                entry<Routes.HomeScreen> {
                    HomeScreen(
                        onEditProfile = {

                        },
                        onSeeMoreProfile = {
                        },
                        onLogFoodClick = {
                            navBackStack.navigateSingleTop(Routes.FoodDatabaseScreen)
                        }
                    )
                }
                entry<Routes.ProgressScreen> { ProgressScreen() }
                entry<Routes.SettingsScreen> {
                    SettingsScreen(
                        onPrivacyPolicyClick = {},
                        onTermsClick = {},
                        onRateAppClick = {},
                        onSignedOut = onSignedOut,
                        onAccountDeleted = onAccountDeleted,
                    )
                }

                entry<Routes.FoodDatabaseScreen> {
                    FoodDatabaseScreen(
                        onBack = {
                            navBackStack.removeLastOrNull()
                            navBackStack.navigateSingleTop(Routes.HomeScreen)
                        }
                    )
                }

                entry<Routes.BarcodeScanScreen> {
                    BarcodeScanScreen(
                        onBack = {
                            navBackStack.removeLastOrNull()
                            navBackStack.navigateSingleTop(Routes.HomeScreen)
                        },
                        onLogged = {
                            navBackStack.removeLastOrNull()
                            navBackStack.navigateSingleTop(Routes.HomeScreen)
                        }
                    )
                }

            },
        )
    }
}
