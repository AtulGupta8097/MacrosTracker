package com.example.responsiveapp.presentation.main_screen.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.InsertChartOutlined
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.InsertChartOutlined
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.responsiveapp.core.navigation.Routes

sealed class Navbar(
    val route: Routes,
    val title: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
) {
    object Home: Navbar(route = Routes.HomeScreen, title = "Home", Icons.Filled.Home, Icons.Outlined.Home)
    object Progress: Navbar(route = Routes.ProgressScreen,"Progress", Icons.Filled.InsertChartOutlined, Icons.Outlined.InsertChartOutlined)
    object Goal: Navbar(route = Routes.GoalsScreen,"Goal", Icons.Filled.Flag, Icons.Outlined.Flag)
    object Settings: Navbar(route = Routes.SettingsScreen,"Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
}