package com.macrivo.app.presentation.home.componet.nutrition

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.macrivo.app.presentation.home.model.MacroTargetField
import com.macrivo.app.presentation.ui.theme.spacing
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

private enum class DeltaKind { Invalid, Unchanged, Changed }
private data class DeltaMessage(val text: String, val kind: DeltaKind)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MacroTargetEditSheet(
    field: MacroTargetField,
    currentValue: Int,
    accentColor: Color,
    icon: ImageVector,
    onSave: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var text by remember(field) { mutableStateOf(currentValue.toString()) }
    val value = text.toIntOrNull()
    val inRange = value != null && value in field.min..field.max
    val delta = (value ?: currentValue) - currentValue
    val canSave = inRange && delta != 0

    // staggered entrance
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    fun close(after: () -> Unit) {
        scope.launch { sheetState.hide() }.invokeOnCompletion { after() }
    }

    fun nudge(step: Int) {
        val base = value ?: currentValue
        text = (base + step).coerceIn(field.min, field.max).toString()
    }

    val message = when {
        !inRange -> DeltaMessage("Enter ${field.min}-${field.max} ${field.unit}", DeltaKind.Invalid)
        delta == 0 -> DeltaMessage("No change", DeltaKind.Unchanged)
        else -> DeltaMessage(
            (if (delta > 0) "+" else "-") + "${abs(delta)} ${field.unit} from current",
            DeltaKind.Changed,
        )
    }

    val underline by animateColorAsState(
        targetValue = if (inRange) accentColor else MaterialTheme.colorScheme.error,
        animationSpec = tween(250),
        label = "targetUnderline",
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(300)) + slideInVertically(tween(380)) { it / 8 },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(
                        start = MaterialTheme.spacing.lg,
                        end = MaterialTheme.spacing.lg,
                        bottom = MaterialTheme.spacing.md,
                    ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
            ) {

                // ---- header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                    Column {
                        Text(
                            text = "${field.label} target",
                            style = MaterialTheme.typography.titleMedium
                                .copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = "Currently $currentValue ${field.unit} per day",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                // ---- stepper + big number
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    StepButton(
                        icon = Icons.Default.Remove,
                        description = "Decrease ${field.label}",
                        accentColor = accentColor,
                        onClick = { nudge(-field.step) },
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        BasicTextField(
                            value = text,
                            onValueChange = { new ->
                                // digits only, max 4 chars (largest allowed value is 6000)
                                if (new.length <= 4 && new.all { it.isDigit() }) text = new
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = 46.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = if (inRange) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.error,
                            ),
                            cursorBrush = SolidColor(accentColor),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                        Box(
                            modifier = Modifier
                                .width(96.dp)
                                .height(3.dp)
                                .clip(CircleShape)
                                .background(underline),
                        )
                        Spacer(Modifier.height(MaterialTheme.spacing.xs))
                        Text(
                            text = field.unit,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    StepButton(
                        icon = Icons.Default.Add,
                        description = "Increase ${field.label}",
                        accentColor = accentColor,
                        onClick = { nudge(field.step) },
                    )
                }

                // ---- live message (cross-fades)
                Crossfade(
                    targetState = message,
                    animationSpec = tween(200),
                    modifier = Modifier.fillMaxWidth(),
                    label = "deltaMessage",
                ) { m ->
                    Text(
                        text = m.text,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                            .copy(fontWeight = FontWeight.SemiBold),
                        color = when (m.kind) {
                            DeltaKind.Invalid -> MaterialTheme.colorScheme.error
                            DeltaKind.Unchanged -> MaterialTheme.colorScheme.onSurfaceVariant
                            DeltaKind.Changed -> accentColor
                        },
                    )
                }

                // ---- slider (coarse changes), snapped to the field's step
                Column {
                    Slider(
                        value = (value ?: currentValue).coerceIn(field.min, field.max).toFloat(),
                        onValueChange = { raw ->
                            val snapped = ((raw - field.min) / field.step).roundToInt() * field.step + field.min
                            text = snapped.coerceIn(field.min, field.max).toString()
                        },
                        valueRange = field.min.toFloat()..field.max.toFloat(),
                        colors = SliderDefaults.colors(
                            thumbColor = accentColor,
                            activeTrackColor = accentColor,
                            inactiveTrackColor = accentColor.copy(alpha = 0.18f),
                        ),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.sm),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "${field.min} ${field.unit}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "${field.max} ${field.unit}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                // ---- actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.xs),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
                ) {
                    OutlinedButton(
                        onClick = { close(onDismiss) },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) { Text("Cancel") }

                    Button(
                        onClick = { value?.let { v -> close { onSave(v) } } },
                        enabled = canSave,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) { Text("Save", fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

@Composable
private fun StepButton(
    icon: ImageVector,
    description: String,
    accentColor: Color,
    onClick: () -> Unit,
) {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.86f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "stepScale",
    )

    FilledTonalIconButton(
        onClick = onClick,
        interactionSource = source,
        modifier = Modifier
            .size(52.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = accentColor.copy(alpha = 0.16f),
            contentColor = accentColor,
        ),
    ) {
        Icon(imageVector = icon, contentDescription = description)
    }
}
