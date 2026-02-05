package com.example.responsiveapp.presentation.user_setup_screens.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.responsiveapp.domain.model.MacroNutrients
import com.example.responsiveapp.presentation.ui.theme.DeviceConfiguration
import com.example.responsiveapp.presentation.ui.theme.deviceConfiguration
import com.example.responsiveapp.presentation.ui.theme.spacing
import kotlinx.coroutines.delay

@Composable
fun UserSetupCompleteScreen(
    isSaving: Boolean,
    macros: MacroNutrients?,
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val deviceConfig = MaterialTheme.deviceConfiguration

    val maxContentWidth = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> 600.dp
        DeviceConfiguration.TABLET -> 520.dp
        DeviceConfiguration.MOBILE -> 9999.dp
    }

    val horizontalPadding = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> MaterialTheme.spacing.xl
        DeviceConfiguration.TABLET -> MaterialTheme.spacing.lg
        DeviceConfiguration.MOBILE -> MaterialTheme.spacing.md
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            when {
                isSaving -> {
                    CompleteLoadingContent(deviceConfig)
                }
                macros != null -> {
                    CompleteSuccessContent(
                        macros = macros,
                        maxWidth = maxContentWidth,
                        horizontalPadding = horizontalPadding,
                        deviceConfig = deviceConfig
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = !isSaving && macros != null,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(600, delayMillis = 1000)
            ) + fadeIn(animationSpec = tween(600, delayMillis = 1000))
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .navigationBarsPadding(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 16.dp,
                tonalElevation = 3.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = onNavigateToMain,
                        modifier = Modifier
                            .padding(horizontalPadding, MaterialTheme.spacing.md)
                            .widthIn(max = maxContentWidth)
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        Text(
                            text = "Let's Get Started",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Navigate",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CompleteLoadingContent(deviceConfig: DeviceConfiguration) {
    val rotation = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        rotation.animateTo(
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    val loadingSize = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> 160.dp
        DeviceConfiguration.TABLET -> 150.dp
        DeviceConfiguration.MOBILE -> 140.dp
    }

    val progressSize = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> 80.dp
        DeviceConfiguration.TABLET -> 75.dp
        DeviceConfiguration.MOBILE -> 70.dp
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Box(
            modifier = Modifier
                .size(loadingSize)
                .scale(scale.value)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(progressSize)
                    .rotate(rotation.value),
                strokeWidth = 5.dp,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.xl))

        Text(
            text = "Setting things up…",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.sm))

        Text(
            text = "Creating your personalized nutrition plan",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CompleteSuccessContent(
    macros: MacroNutrients,
    maxWidth: Dp,
    horizontalPadding: Dp,
    deviceConfig: DeviceConfiguration
) {
    val scrollState = rememberScrollState()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .statusBarsPadding()
            .padding(horizontal = horizontalPadding)
            .widthIn(max = maxWidth)
            .padding(top = MaterialTheme.spacing.lg, bottom = 90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(800)) + scaleIn(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                initialScale = 0.3f
            )
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                SuccessIcon(deviceConfig)

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.lg))

                Text(
                    text = "You're All Set! 🎉",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = when (deviceConfig) {
                            DeviceConfiguration.DESKTOP -> 42.sp
                            DeviceConfiguration.TABLET -> 38.sp
                            DeviceConfiguration.MOBILE -> 32.sp
                        }
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.sm))

                Text(
                    text = "Here's your personalized daily nutrition target",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.xxl))

        CaloriesCard(calories = macros.calories, deviceConfig = deviceConfig)

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.xl))

        MacroCardsGrid(macros = macros, deviceConfig = deviceConfig)

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.lg))

        BmrCard(bmr = macros.bmr, deviceConfig = deviceConfig)
    }
}

@Composable
private fun SuccessIcon(deviceConfig: DeviceConfiguration) {
    var visible by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(150)
        visible = true
        rotation.animateTo(
            targetValue = 360f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "success_icon_scale"
    )

    val iconSize = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> 120.dp
        DeviceConfiguration.TABLET -> 110.dp
        DeviceConfiguration.MOBILE -> 100.dp
    }

    val checkSize = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> 64.dp
        DeviceConfiguration.TABLET -> 60.dp
        DeviceConfiguration.MOBILE -> 56.dp
    }

    Box(
        modifier = Modifier
            .size(iconSize)
            .scale(scale)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            modifier = Modifier
                .size(checkSize)
                .rotate(rotation.value),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun CaloriesCard(calories: Int, deviceConfig: DeviceConfiguration) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        visible = true
    }

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "calories_scale"
    )

    val iconSize = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> 34.dp
        DeviceConfiguration.TABLET -> 32.dp
        DeviceConfiguration.MOBILE -> 28.dp
    }

    val titleSize = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> 22.sp
        DeviceConfiguration.TABLET -> 20.sp
        DeviceConfiguration.MOBILE -> 16.sp
    }

    val calorieSize = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> 76.sp
        DeviceConfiguration.TABLET -> 72.sp
        DeviceConfiguration.MOBILE -> 64.sp
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(500))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocalFireDepartment,
                        contentDescription = "Calories",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(iconSize)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Daily Calorie Target",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = titleSize
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))

                Text(
                    text = "$calories",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = calorieSize
                    ),
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "calories per day",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = when (deviceConfig) {
                            DeviceConfiguration.DESKTOP -> 17.sp
                            DeviceConfiguration.TABLET -> 16.sp
                            DeviceConfiguration.MOBILE -> 14.sp
                        }
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun MacroCardsGrid(macros: MacroNutrients, deviceConfig: DeviceConfiguration) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(600))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MacroCard(
                name = "Protein",
                value = macros.protein,
                unit = "g",
                gradient = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF6B46C1).copy(alpha = 0.9f),
                        Color(0xFF9333EA).copy(alpha = 0.9f)
                    )
                ),
                modifier = Modifier.weight(1f),
                delay = 0,
                animationType = MacroAnimationType.SLIDE_LEFT,
                deviceConfig = deviceConfig
            )

            MacroCard(
                name = "Carbs",
                value = macros.carbs,
                unit = "g",
                gradient = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0891B2).copy(alpha = 0.9f),
                        Color(0xFF06B6D4).copy(alpha = 0.9f)
                    )
                ),
                modifier = Modifier.weight(1f),
                delay = 150,
                animationType = MacroAnimationType.ZOOM_CENTER,
                deviceConfig = deviceConfig
            )

            MacroCard(
                name = "Fats",
                value = macros.fats,
                unit = "g",
                gradient = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFDC2626).copy(alpha = 0.9f),
                        Color(0xFFF87171).copy(alpha = 0.9f)
                    )
                ),
                modifier = Modifier.weight(1f),
                delay = 300,
                animationType = MacroAnimationType.SLIDE_RIGHT,
                deviceConfig = deviceConfig
            )
        }
    }
}

enum class MacroAnimationType {
    SLIDE_LEFT,
    SLIDE_RIGHT,
    ZOOM_CENTER
}

@Composable
private fun MacroCard(
    name: String,
    value: Int,
    unit: String,
    gradient: Brush,
    modifier: Modifier = Modifier,
    delay: Long = 0,
    animationType: MacroAnimationType,
    deviceConfig: DeviceConfiguration
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(delay)
        visible = true
    }

    val enterAnimation = when (animationType) {
        MacroAnimationType.SLIDE_LEFT -> slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(animationSpec = tween(500))

        MacroAnimationType.SLIDE_RIGHT -> slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(animationSpec = tween(500))

        MacroAnimationType.ZOOM_CENTER -> scaleIn(
            initialScale = 0.3f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(animationSpec = tween(500))
    }

    val verticalPadding = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> 28.dp
        DeviceConfiguration.TABLET -> 24.dp
        DeviceConfiguration.MOBILE -> 20.dp
    }

    val horizontalPadding = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> 18.dp
        DeviceConfiguration.TABLET -> 16.dp
        DeviceConfiguration.MOBILE -> 12.dp
    }

    val labelSize = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> 15.sp
        DeviceConfiguration.TABLET -> 14.sp
        DeviceConfiguration.MOBILE -> 13.sp
    }

    val valueSize = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> 44.sp
        DeviceConfiguration.TABLET -> 42.sp
        DeviceConfiguration.MOBILE -> 36.sp
    }

    val unitSize = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> 17.sp
        DeviceConfiguration.TABLET -> 16.sp
        DeviceConfiguration.MOBILE -> 14.sp
    }

    AnimatedVisibility(
        visible = visible,
        enter = enterAnimation,
        modifier = modifier
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(gradient)
                    .padding(
                        vertical = verticalPadding,
                        horizontal = horizontalPadding
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            fontSize = labelSize
                        ),
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$value",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = valueSize
                        ),
                        color = Color.White
                    )

                    Text(
                        text = unit,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = unitSize
                        ),
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
private fun BmrCard(bmr: Int, deviceConfig: DeviceConfiguration) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(800)
        visible = true
    }

    val titleSize = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> 17.sp
        DeviceConfiguration.TABLET -> 16.sp
        DeviceConfiguration.MOBILE -> 14.sp
    }

    val valueSize = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> 26.sp
        DeviceConfiguration.TABLET -> 24.sp
        DeviceConfiguration.MOBILE -> 20.sp
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it / 2 },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(animationSpec = tween(600))
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.md),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )  {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Basal Metabolic Rate",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = titleSize
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Your body burns this at rest",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }

                Text(
                    text = "$bmr cal",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = valueSize
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}