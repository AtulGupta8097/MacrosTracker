package com.example.responsiveapp.presentation.main_screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BottomNav(
    items: List<Navbar>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
) {
    val density = LocalDensity.current
    val fabRadius = with(density) { 28.dp.toPx() }

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = BottomBarCutoutShape(
            fabRadius = fabRadius,
            cutoutMargin = fabRadius * 0.25f,
        ),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
        tonalElevation = 8.dp,
    ) {
        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.take(items.size / 2).forEachIndexed { index, item ->
                    BottomNavItem(
                        item = item,
                        selected = selectedTab == index,
                        onClick = { onTabSelected(index) },
                    )
                }

                Spacer(modifier = Modifier.width(48.dp))

                items.drop(items.size / 2).forEachIndexed { index, item ->
                    val actualIndex = index + items.size / 2
                    BottomNavItem(
                        item = item,
                        selected = selectedTab == actualIndex,
                        onClick = { onTabSelected(actualIndex) },
                    )
                }
            }

            // This spacer fills exactly the system nav bar height, pushing surface behind it
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}
