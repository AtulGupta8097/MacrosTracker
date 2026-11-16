package com.macrivo.app.presentation.mainscreen.component

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.macrivo.app.presentation.ui.theme.spacing
import com.macrivo.app.presentation.mainscreen.AddFoodOption
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodBottomSheet(
    onDismiss: () -> Unit,
    onOptionSelected: (String) -> Unit,
) {
    val sheetState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )

    val hero =
        AddFoodOption(
            title = "Food Database",
            subtitle = "Search thousands of foods",
            icon = Icons.Outlined.Book,
            iconColor = Color.White,
            backgroundColor = Color.White.copy(alpha = 0.2f),
        )

    val quickOptions =
        listOf(
            AddFoodOption(
                title = "Scan Barcode",
                subtitle = "Packaged food, instantly",
                icon = Icons.Outlined.QrCodeScanner,
                iconColor = Color(0xFF0EA5E9),
                backgroundColor = Color(0xFF0EA5E9).copy(alpha = 0.14f),
            ),
            AddFoodOption(
                title = "Saved Foods",
                subtitle = "Your favorites, one tap",
                icon = Icons.Outlined.Star,
                iconColor = Color(0xFFF59E0B),
                backgroundColor = Color(0xFFF59E0B).copy(alpha = 0.16f),
            ),
        )

    val select: (AddFoodOption) -> Unit = { option ->
        onOptionSelected(option.title)
        onDismiss()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = {
            Box(
                modifier =
                    Modifier
                        .padding(top = MaterialTheme.spacing.md - MaterialTheme.spacing.xs, bottom = MaterialTheme.spacing.sm)
                        .width(44.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)),
            )
        },
    ) {
        val spacing = MaterialTheme.spacing
        val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

        // scrolls (landscape sheets are shorter than their content) and clears the navigation bar
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(horizontal = spacing.md)
                    .padding(top = spacing.xs, bottom = spacing.lg),
        ) {
            SheetHeader(modifier = Modifier.staggeredEntrance(0))

            Spacer(modifier = Modifier.height(spacing.lg))

            if (isLandscape) {
                // one row: hero a little wider than the two tiles
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(spacing.md),
                ) {
                    HeroOptionCard(
                        option = hero,
                        onClick = { select(hero) },
                        modifier = Modifier.weight(1.4f).fillMaxHeight().staggeredEntrance(1),
                    )
                    quickOptions.forEachIndexed { index, option ->
                        QuickOptionTile(
                            option = option,
                            onClick = { select(option) },
                            modifier = Modifier.weight(1f).fillMaxHeight().staggeredEntrance(index + 2),
                        )
                    }
                }
            } else {
                HeroOptionCard(
                    option = hero,
                    onClick = { select(hero) },
                    modifier = Modifier.fillMaxWidth().staggeredEntrance(1),
                )

                Spacer(modifier = Modifier.height(spacing.md))

                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(spacing.md),
                ) {
                    quickOptions.forEachIndexed { index, option ->
                        QuickOptionTile(
                            option = option,
                            onClick = { select(option) },
                            modifier = Modifier.weight(1f).fillMaxHeight().staggeredEntrance(index + 2),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SheetHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(26.dp),
            )
        }

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.md))

        Column {
            Text(
                text = "Add Food",
                // headlineMedium is the largest heading the app theme defines in Poppins (headlineSmall is not
                // defined there, so it fell back to the system font)
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "Choose how you'd like to log your meal",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** The main action: a full-width gradient card. */
@Composable
private fun HeroOptionCard(
    option: AddFoodOption,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val primary = MaterialTheme.colorScheme.primary
    val brush =
        Brush.linearGradient(
            colors = listOf(primary, lerp(primary, Color.Black, 0.28f)),
        )

    val (interaction, scale) = rememberPressScale()

    Surface(
        onClick = onClick,
        interactionSource = interaction,
        modifier = modifier.graphicsLayer { scaleX = scale; scaleY = scale },
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent,
    ) {
        Box(
            modifier =
                Modifier
                    .background(brush)
                    .heightIn(min = 132.dp),
        ) {
            // soft decorative circles
            Box(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 36.dp, y = (-36).dp)
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.10f)),
            )
            Box(
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-64).dp, y = 44.dp)
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.07f)),
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.lg),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconBubble(option = option, size = 60.dp, corner = 20.dp)

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.md))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = option.title,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.xxs))
                    Text(
                        text = option.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.88f),
                    )
                }

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.md))

                Box(
                    modifier =
                        Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.22f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
    }
}

/** A secondary action: a tinted tile with the icon on top. */
@Composable
private fun QuickOptionTile(
    option: AddFoodOption,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (interaction, scale) = rememberPressScale()

    Surface(
        onClick = onClick,
        interactionSource = interaction,
        modifier = modifier.graphicsLayer { scaleX = scale; scaleY = scale },
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
        border = BorderStroke(1.dp, option.iconColor.copy(alpha = 0.22f)),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 132.dp)
                    .padding(MaterialTheme.spacing.md),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            IconBubble(option = option, size = 48.dp, corner = 16.dp)

            Column {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.xxs))
                Text(
                    text = option.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun IconBubble(
    option: AddFoodOption,
    size: androidx.compose.ui.unit.Dp,
    corner: androidx.compose.ui.unit.Dp,
) {
    Box(
        modifier =
            Modifier
                .size(size)
                .clip(RoundedCornerShape(corner))
                .background(option.backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = option.icon,
            contentDescription = null, // the title next to it already names the action
            tint = option.iconColor,
            modifier = Modifier.size(size * 0.5f),
        )
    }
}

/** A press interaction source and the scale (1 -> 0.96) it drives. */
@Composable
private fun rememberPressScale(): Pair<MutableInteractionSource, Float> {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "pressScale",
    )
    return interaction to scale
}

/** Fades and slides an item up shortly after the sheet opens; [index] staggers the items. */
@Composable
private fun Modifier.staggeredEntrance(index: Int): Modifier {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay((70L * index).milliseconds)
        progress.animateTo(1f, tween(durationMillis = 380, easing = FastOutSlowInEasing))
    }
    return graphicsLayer {
        alpha = progress.value
        translationY = (1f - progress.value) * 28.dp.toPx()
    }
}
