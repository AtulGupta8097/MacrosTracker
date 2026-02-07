package com.example.responsiveapp.presentation.main_screen.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavItem(
    modifier: Modifier = Modifier,
    item: Navbar,
    selected: Boolean,
    onClick: () -> Unit
) {
    // Animations
    val animatedIconSize by animateDpAsState(
        targetValue = if (selected) 26.dp else 20.dp,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "iconSize"
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = if (selected) 1f else 0.6f,
        label = "alpha"
    )

    val animatedElevation by animateDpAsState(
        targetValue = if (selected) 8.dp else 0.dp,
        label = "elevation"
    )

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Icon container (gives elevation effect)
        Box(
            modifier = Modifier
                .padding(4.dp)
                .graphicsLayer {
                    shadowElevation= animatedElevation.toPx()
                }
        ) {
            FlipIcon(
                modifier = Modifier
                    .size(animatedIconSize)
                    .alpha(animatedAlpha),
                isActive = selected,
                activeIcon = item.activeIcon,
                inactiveIcon = item.inactiveIcon,
                contentDescription = item.title,
                color = if (selected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                rotationMax = 180f,
                rotationMin = 0f
            )
        }

        // Label only when selected (same as DreamTrade)
        if (selected) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


