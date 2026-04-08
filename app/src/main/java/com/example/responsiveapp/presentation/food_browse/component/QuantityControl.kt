package com.example.responsiveapp.presentation.food_browse.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.responsiveapp.presentation.ui.theme.spacing
import kotlin.math.roundToInt

@Composable
fun QuantityControl(
    quantity: Float,
    onQuantityChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayText = if (quantity == quantity.roundToInt().toFloat())
        quantity.roundToInt().toString()
    else "%.1f".format(quantity)

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)) {
        Text(
            text = "QUANTITY",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold, letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            shadowElevation = 1.dp,
        ) {
            Row(
                modifier = Modifier.padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(40.dp).clickable {
                        onQuantityChanged((quantity - 0.5f).coerceAtLeast(0.5f))
                    }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Remove, contentDescription = "Decrease",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                AnimatedContent(
                    targetState = displayText,
                    transitionSpec = { (slideInVertically { -it } + fadeIn()) togetherWith fadeOut() },
                    label = "QtyAnim"
                ) { text ->
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.width(72.dp),
                        textAlign = TextAlign.Center,
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp).clickable {
                        onQuantityChanged(quantity + 0.5f)
                    }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Add, contentDescription = "Increase",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}