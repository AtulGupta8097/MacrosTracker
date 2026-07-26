package com.example.responsiveapp.presentation.home.componet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val AvatarBorderWidth = 2.dp

@Composable
fun ProfileAvatar(
    modifier: Modifier = Modifier,
    name: String?,
    size: Dp,
    showBorder: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val initial = remember(name) {
        name
            ?.trim()
            ?.firstOrNull()
            ?.uppercaseChar()
            ?.toString()
            ?: "?"
    }

    Box(
        modifier = modifier
            .size(size)
            .then(
                if (showBorder) {
                    Modifier.shadow(
                        elevation = size / 10,
                        shape = CircleShape,
                        clip = false
                    )
                } else {
                    Modifier
                }
            )
            .clip(CircleShape)
            .background(
                MaterialTheme.colorScheme.surface
            ).then(
                if (showBorder) {
                    Modifier.border(
                        width = AvatarBorderWidth,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                } else {
                    Modifier
                }
            )
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = initial,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}