package com.example.responsiveapp.presentation.common_component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.CustomToastProperty
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun CustomToast(
    message: String,
    type: CustomToastProperty,
    durationMillis: Long = 3000,
    onDismiss: () -> Unit,
    visibility: Boolean
) {
    val progress = remember { Animatable(1f) }

    LaunchedEffect(visibility) {
        if (visibility) {
            progress.snapTo(1f)
            progress.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis.toInt(), easing = LinearEasing)
            )
            onDismiss()
        }
    }

    // Slide-Up Animation
    val animatedOffset by animateFloatAsState(
        targetValue = 0f,
        animationSpec = tween(800, easing = LinearOutSlowInEasing),
        label = "toast-slide-up"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .background(Color.Transparent)
            .offset(y = animatedOffset.dp)
            .imePadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = visibility,
            enter = scaleIn(
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            ),
            exit = scaleOut()
        ) {
            Column(
                modifier = Modifier
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(MaterialTheme.spacing.sm),
                        clip = false
                    )
                    .clip(RoundedCornerShape(MaterialTheme.spacing.sm)) // 🔑 THIS
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(MaterialTheme.spacing.sm)
                    )
                    .border(
                        width = 1.dp,
                        color = type.borderColor(),
                        shape = RoundedCornerShape(MaterialTheme.spacing.sm)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Icon(
                            imageVector = type.icon(),
                            contentDescription = "Toast Icon",
                            tint = type.progressBarColor(),
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.sm))
                        Text(
                            text = message,
                            modifier = Modifier.padding(start = 10.dp, end = 15.dp, top = 5.dp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Toast Icon",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(25.dp)
                            .align(AbsoluteAlignment.CenterRight)
                            .clickable {
                                onDismiss()
                            }
                    )
                }

                // Smooth Progress Bar
                LinearProgressIndicator(
                    progress = progress.value,
                    color = type.progressBarColor(),
                    trackColor = Color.LightGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(MaterialTheme.spacing.xs)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCustomToast() {
    CustomToast(
        message = "This is a preview toast message",
        type = ErrorToast(),
        durationMillis = 3000,
        onDismiss = {},
        visibility = true
    )

}



