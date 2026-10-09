package com.example.responsiveapp.presentation.home.componet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.responsiveapp.presentation.home.model.StreakDayUiModel
import com.example.responsiveapp.presentation.home.model.StreakWeekUiState
import com.example.responsiveapp.presentation.ui.theme.spacing

private val PopupMaxWidth = 340.dp
private val PopupOffset = IntOffset(x = -16, y = 90)
private const val PopupEnterFadeDuration = 160
private const val PopupExitFadeDuration = 120
private const val PopupWidthFraction = 0.86f
private const val PopupInitialScale = 0.85f
private const val PopupTargetScale = 0.85f

private val DayCircleSize = 40.dp

@Composable
fun StreakWeekPopup(
    streak: StreakWeekUiState,
    containerMaxWidth: Dp,
    onDismiss: () -> Unit,
) {
    var visible by remember {
        mutableStateOf(false)
    }

    val popupWidth = remember(containerMaxWidth) {
        minOf(containerMaxWidth * PopupWidthFraction, PopupMaxWidth)
    }

    Popup(
        alignment = Alignment.TopEnd,
        offset = PopupOffset,
        onDismissRequest = onDismiss,
        properties = PopupProperties(
            focusable = true
        )
    ) {

        LaunchedEffect(Unit) {
            visible = true
        }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(
                animationSpec = tween(PopupEnterFadeDuration)
            ) + scaleIn(
                initialScale = PopupInitialScale,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy
                )
            ),
            exit = fadeOut(
                animationSpec = tween(PopupExitFadeDuration)
            ) + scaleOut(
                targetScale = PopupTargetScale
            )
        ) {

            Surface(
                modifier = Modifier.width(popupWidth),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.background,
                shadowElevation = 8.dp
            ) {

                Column(
                    modifier = Modifier.padding(MaterialTheme.spacing.md)
                ) {

                    StreakHeader(streak = streak)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MaterialTheme.spacing.md),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        streak.days.forEach { day ->
                            StreakDayCircle(day = day)
                        }
                    }

                    StreakFooterMessage(
                        streak = streak,
                        modifier = Modifier.padding(top = MaterialTheme.spacing.md),
                    )
                }
            }
        }
    }
}

@Composable
private fun StreakHeader(streak: StreakWeekUiState) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp),
            )
        }

        Column(
            modifier = Modifier.padding(start = MaterialTheme.spacing.sm)
        ) {
            Text(
                text = "${streak.currentStreakText} Day${if (streak.currentStreakCount == 1) "" else "s"} Streak",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "This week",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun StreakDayCircle(day: StreakDayUiModel) {

    val circleBackground = when {
        day.isLogged -> Brush.linearGradient(
            listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.75f),
            )
        )
        day.isToday -> Brush.linearGradient(
            listOf(
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.primaryContainer,
            )
        )
        else -> Brush.linearGradient(
            listOf(
                MaterialTheme.colorScheme.surfaceVariant,
                MaterialTheme.colorScheme.surfaceVariant,
            )
        )
    }

    val contentColor = when {
        day.isLogged -> MaterialTheme.colorScheme.onPrimary
        day.isToday -> MaterialTheme.colorScheme.primary
        day.isFuture -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = day.weekdayLabel.take(1),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = if (day.isToday) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            },
        )

        Box(
            modifier = Modifier
                .padding(top = MaterialTheme.spacing.xs)
                .size(DayCircleSize)
                .clip(CircleShape)
                .background(circleBackground),
            contentAlignment = Alignment.Center,
        ) {
            if (day.isLogged) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Logged",
                    tint = contentColor,
                    modifier = Modifier.size(18.dp),
                )
            } else {
                Text(
                    text = day.dayNumber,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor,
                )
            }
        }
    }
}

@Composable
private fun StreakFooterMessage(
    streak: StreakWeekUiState,
    modifier: Modifier = Modifier,
) {
    val todayLogged = streak.days.any { it.isToday && it.isLogged }

    val message = when {
        todayLogged -> "You're all set for today. Keep it up!"
        streak.currentStreakCount > 0 -> "Log a meal today to keep your streak going."
        else -> "Log a meal today to start a new streak."
    }

    Text(
        text = message,
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
