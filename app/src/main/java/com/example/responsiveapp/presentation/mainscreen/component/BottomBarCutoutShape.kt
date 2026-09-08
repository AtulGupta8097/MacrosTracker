package com.example.responsiveapp.presentation.mainscreen.component

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class BottomBarCutoutShape(
    private val fabRadius: Float,
    private val cutoutMargin: Float,
    private val notchCenterX: Float,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val path =
            Path().apply {
                val cutoutRadius = fabRadius + cutoutMargin

                moveTo(0f, 0f)

                lineTo(notchCenterX - cutoutRadius * 2, 0f)

                cubicTo(
                    notchCenterX - cutoutRadius,
                    0f,
                    notchCenterX - cutoutRadius,
                    cutoutRadius,
                    notchCenterX,
                    cutoutRadius,
                )

                cubicTo(
                    notchCenterX + cutoutRadius,
                    cutoutRadius,
                    notchCenterX + cutoutRadius,
                    0f,
                    notchCenterX + cutoutRadius * 2,
                    0f,
                )

                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }

        return Outline.Generic(path)
    }
}