
package com.example.responsiveapp.presentation.commoncomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme

@Composable
fun CircularIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val glowColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .size(44.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .blur(16.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                .background(glowColor.copy(alpha = 0.7f), CircleShape)
                .size(36.dp)
        )

        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = glowColor,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrevCircularIconButton() {
    ResponsiveAppTheme {
        CircularIconButton(
            icon = Icons.Default.Add,
            contentDescription = "Log meal",
            onClick = {}
        )
    }
}
