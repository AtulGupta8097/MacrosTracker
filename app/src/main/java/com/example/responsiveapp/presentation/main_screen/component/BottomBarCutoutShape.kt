package com.example.responsiveapp.presentation.main_screen.component

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class BottomBarCutoutShape(
    private val fabRadius: Float,
    private val cutoutMargin: Float
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val path = Path().apply {
            val centerX = size.width / 2
            val cutoutRadius = fabRadius + cutoutMargin

            moveTo(0f, 0f)

            lineTo(centerX - cutoutRadius * 2, 0f)

            cubicTo(
                centerX - cutoutRadius, 0f,
                centerX - cutoutRadius, cutoutRadius,
                centerX, cutoutRadius
            )

            cubicTo(
                centerX + cutoutRadius, cutoutRadius,
                centerX + cutoutRadius, 0f,
                centerX + cutoutRadius * 2, 0f
            )

            lineTo(size.width, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }

        return Outline.Generic(path)
    }
}
