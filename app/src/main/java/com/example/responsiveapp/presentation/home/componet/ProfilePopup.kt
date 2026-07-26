package com.example.responsiveapp.presentation.home.componet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Flag
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.responsiveapp.domain.model.UserProfile
import com.example.responsiveapp.presentation.ui.theme.spacing

private val PopupWidth = 260.dp
private val PopupOffset = IntOffset(x = -16, y = 90)
private const val PopupEnterFadeDuration = 160
private const val PopupExitFadeDuration = 120

private const val PopupInitialScale = 0.85f
private const val PopupTargetScale = 0.85f

@Composable
fun ProfilePopup(
    userProfile: UserProfile?,
    onDismiss: () -> Unit,
    onEditProfile: () -> Unit,
    onSeeMoreProfile: () -> Unit,
) {
    var visible by remember {
        mutableStateOf(false)
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
                modifier = Modifier.width(PopupWidth),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {

                Column(
                    modifier = Modifier.padding(
                        MaterialTheme.spacing.md
                    )
                ) {

                    ProfileHeader(
                        userProfile = userProfile
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            vertical = MaterialTheme.spacing.sm
                        )
                    )

                    userProfile?.let { profile ->

                        InfoRow(
                            icon = Icons.Default.Flag,
                            label = profile.goal.label
                        )

                        InfoRow(
                            modifier = Modifier.padding(
                                top = MaterialTheme.spacing.xs
                            ),
                            icon = Icons.Default.Bolt,
                            label = profile.activityLevel.label
                        )
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



