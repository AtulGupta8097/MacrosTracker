package com.example.responsiveapp.presentation.myfood.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.myfood.CreateFoodStep
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme

// CHANGED: new file, replaces StepIndicator.kt (which had no click handling at all).
// Two equal-width, tappable segments with a sliding highlight. Labels only - no step numbers.
@Composable
fun CreateFoodStepSwitcher(
    currentStep: CreateFoodStep,
    onStepSelected: (CreateFoodStep) -> Unit,
    modifier: Modifier = Modifier,
) {
    val steps = listOf(
        CreateFoodStep.BASIC_INFO to "Basic Info",
        CreateFoodStep.NUTRIENTS to "Nutrients",
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp) // comfortably above the 48dp minimum touch target
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp),
    ) {
        val segmentWidth = maxWidth / steps.size
        val indicatorOffset by animateDpAsState(
            targetValue = segmentWidth * steps.indexOfFirst { it.first == currentStep },
            animationSpec = tween(durationMillis = 250),
            label = "stepIndicatorOffset",
        )

        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(segmentWidth)
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary),
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            steps.forEach { (step, label) ->
                val selected = step == currentStep
                val textColor by animateColorAsState(
                    targetValue = if (selected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    animationSpec = tween(durationMillis = 250),
                    label = "stepLabelColor",
                )

                Box(
                    modifier = Modifier
                        .width(segmentWidth)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .selectable(
                            selected = selected,
                            role = Role.Tab,
                            onClick = { onStepSelected(step) },
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        ),
                        color = textColor,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewStepSwitcher() {
    ResponsiveAppTheme {
        CreateFoodStepSwitcher(
            currentStep = CreateFoodStep.NUTRIENTS,
            onStepSelected = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
