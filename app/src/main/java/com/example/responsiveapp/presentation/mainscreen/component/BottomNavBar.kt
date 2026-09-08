package com.example.responsiveapp.presentation.mainscreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

private val FabSize = 56.dp
private val FabEdgeMargin = 20.dp
private val FabFloatGap = 12.dp
private const val CutoutMarginFraction = 0.25f

@Composable
fun BottomNav(
    items: List<Navbar>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onAddClick: () -> Unit,
) {
    val density = LocalDensity.current
    val fabRadiusPx = with(density) { (FabSize / 2).toPx() }
    val edgeMarginPx = with(density) { FabEdgeMargin.toPx() }

    val cutoutMarginDp = (FabSize / 2) * CutoutMarginFraction
    val curvePeakDepth = (FabSize / 2) + cutoutMarginDp
    val fabOffsetY = curvePeakDepth - FabFloatGap - FabSize

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val barWidthPx = with(density) { maxWidth.toPx() }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = BottomBarCutoutShape(
                fabRadius = fabRadiusPx,
                cutoutMargin = fabRadiusPx * CutoutMarginFraction,
                notchCenterX = barWidthPx - (fabRadiusPx + edgeMarginPx),
            ),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
            tonalElevation = 8.dp,
        ) {
            Column {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(end = FabSize + FabEdgeMargin),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    items.forEachIndexed { index, item ->
                        BottomNavItem(
                            item = item,
                            selected = selectedTab == index,
                            onClick = { onTabSelected(index) },
                        )
                    }
                }

                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }

        FloatingActionButton(
            onClick = onAddClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = FabEdgeMargin)
                .offset(y = fabOffsetY)
                .size(FabSize),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                focusedElevation = 0.dp,
                hoveredElevation = 0.dp,
            ),
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}