package com.example.responsiveapp.presentation.home.componet

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.R
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme

private object BodyIllustrationDefaults {

    val MinSize = 56.dp
    val MaxSize = 128.dp

    val GlowLayers = listOf(
        GlowLayer(scale = 1.65f, alpha = 0.08f),
        GlowLayer(scale = 1.35f, alpha = 0.14f),
        GlowLayer(scale = 1.1f, alpha = 0.20f),
    )
}

private data class GlowLayer(
    val scale: Float,
    val alpha: Float,
)

@Composable
fun BodyIllustration(
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary,
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {

        val illustrationSize = maxWidth.coerceIn(
            minimumValue = BodyIllustrationDefaults.MinSize,
            maximumValue = BodyIllustrationDefaults.MaxSize,
        )
        LayeredGlow(
            modifier = Modifier.size(illustrationSize),
            silhouetteDiameter = illustrationSize,
            tint = tint,
        )

        Image(
            painter = painterResource(R.drawable.ic_body_silhouette),
            contentDescription = null,
            modifier = Modifier.size(illustrationSize),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(tint),
        )
    }
}

@Composable
private fun LayeredGlow(
    silhouetteDiameter: Dp,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier,
    ) {
        val center = Offset(
            x = size.width / 2f,
            y = size.height / 2f,
        )

        BodyIllustrationDefaults.GlowLayers.forEach { layer ->
            drawGlowRing(
                center = center,
                diameter = silhouetteDiameter * layer.scale,
                color = tint,
                alpha = layer.alpha,
            )
        }
    }
}

private fun DrawScope.drawGlowRing(
    center: Offset,
    diameter: Dp,
    color: Color,
    alpha: Float,
) {
    val radius = diameter.toPx() / 2f

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                color.copy(alpha = alpha),
                color.copy(alpha = 0f),
            ),
            center = center,
            radius = radius,
        ),
        radius = radius,
        center = center,
    )
}

@Preview(showBackground = true)
@Composable
private fun BodyIllustrationCompactPreview() {
    ResponsiveAppTheme {
        BodyIllustration(
            modifier = Modifier.size(64.dp),
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 300,
)
@Composable
private fun BodyIllustrationTabletPreview() {
    ResponsiveAppTheme {
        BodyIllustration(
            modifier = Modifier.size(300.dp),
        )
    }
}