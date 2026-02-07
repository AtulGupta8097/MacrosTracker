package com.example.responsiveapp.presentation.main_screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun BottomNav(
    items: List<Navbar>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .shadow(8.dp)
            .background(MaterialTheme.colorScheme.surface)
            .height(68.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // LEFT items (before Add)
            items.take(items.size / 2).forEachIndexed { index, item ->
                BottomNavItem(
                    modifier = Modifier.weight(1f),
                    item = item,
                    selected = selectedTab == index,
                    onClick = { onTabSelected(index) }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // RIGHT items (after Add)
            items.drop(items.size / 2).forEachIndexed { index, item ->
                val actualIndex = index + items.size / 2
                BottomNavItem(
                    modifier = Modifier.weight(1f),
                    item = item,
                    selected = selectedTab == actualIndex,
                    onClick = { onTabSelected(actualIndex) }
                )
            }
        }

        // CENTER ADD BUTTON
        FloatingActionButton(
            onClick = onAddClick,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add"
            )
        }
    }
}

