package com.example.responsiveapp.presentation.main_screen.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SideNav(
    items: List<Navbar>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        header = {
            Spacer(modifier = Modifier.height(16.dp))
        }
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Navigation items
        items.forEachIndexed { index, item ->
            NavigationRailItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        imageVector = if (selectedIndex == index) item.activeIcon else item.inactiveIcon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = when (item.title) {
                            is com.example.responsiveapp.core.navigation.Routes.HomeScreen -> "Home"
                            is com.example.responsiveapp.core.navigation.Routes.ProgressScreen -> "Progress"
                            is com.example.responsiveapp.core.navigation.Routes.GoalsScreen -> "Goals"
                            is com.example.responsiveapp.core.navigation.Routes.SettingsScreen -> "Settings"
                            else -> ""
                        }
                    )
                }
            )

            // Add FAB after second item
            if (index == 1) {
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(
                    onClick = onAddClick,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(56.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}