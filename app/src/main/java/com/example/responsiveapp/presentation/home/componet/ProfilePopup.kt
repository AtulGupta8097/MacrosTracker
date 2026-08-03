package com.example.responsiveapp.presentation.home.componet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.responsiveapp.presentation.home.model.ProfileUiModel
import com.example.responsiveapp.presentation.ui.theme.spacing

private val PopupMaxWidth = 340.dp
private val PopupOffset = IntOffset(x = -16, y = 90)
private const val PopupEnterFadeDuration = 160
private const val PopupExitFadeDuration = 120
private const val PopupWidthFraction = 0.86f


private const val PopupInitialScale = 0.85f
private const val PopupTargetScale = 0.85f

@Composable
fun ProfilePopup(
    profile: ProfileUiModel?,
    containerMaxWidth: Dp,
    onDismiss: () -> Unit,
    onEditProfile: () -> Unit,
    onSeeMoreProfile: () -> Unit,
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
                    modifier = Modifier.padding(
                        MaterialTheme.spacing.md
                    )
                ) {

                    ProfileHeader(
                        profile = profile
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            vertical = MaterialTheme.spacing.sm
                        )
                    )

                    profile?.let { profile ->

                        Column(
                            verticalArrangement = Arrangement.spacedBy(
                                MaterialTheme.spacing.sm
                            )
                        ) {

                            InfoRow(
                                icon = Icons.Default.CalendarToday,
                                label = "Age",
                                value = profile.ageText
                            )

                            InfoRow(
                                icon = Icons.Default.Height,
                                label = "Height",
                                value = profile.heightText
                            )

                            InfoRow(
                                icon = Icons.Default.MonitorWeight,
                                label = "Weight",
                                value = profile.weightText
                            )

                            InfoRow(
                                icon = Icons.Default.TrackChanges,
                                label = "Target Weight",
                                value = profile.targetWeightText
                            )

                            InfoRow(
                                icon = Icons.Default.Bolt,
                                label = "Work Intensity",
                                value = profile.activityLabel
                            )

                            InfoRow(
                                icon = Icons.Default.Flag,
                                label = "Goal",
                                value = profile.goalLabel
                            )
                        }
                    }

                    ProfileActions(
                        modifier = Modifier.padding(
                            top = MaterialTheme.spacing.md
                        ),
                        onEditProfile = onEditProfile,
                        onSeeMoreProfile = onSeeMoreProfile
                    )
                }
            }
        }
    }
}



