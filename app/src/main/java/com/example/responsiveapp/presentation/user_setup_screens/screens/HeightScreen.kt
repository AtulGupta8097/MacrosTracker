package com.example.responsiveapp.presentation.user_setup_screens.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.responsiveapp.presentation.ui.theme.spacing
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class HeightUnit {
    CM, FEET_INCHES
}

@Composable
fun HeightScreen(
    height: Float?, // Always stored in CM
    onHeightChanged: (Float) -> Unit
) {
    var selectedUnit by remember { mutableStateOf(HeightUnit.CM) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.spacing.md)
    ) {
        Text(
            text = "What's your Height?",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )

        Text(
            text = "We'll use this for accurate calculations",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(
                top = MaterialTheme.spacing.sm,
                bottom = MaterialTheme.spacing.lg
            )
        )

        // Unit Selector (Tabs)
        UnitSelector(
            selectedUnit = selectedUnit,
            onUnitChanged = { selectedUnit = it }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.lg))

        // Show appropriate picker based on selected unit
        when (selectedUnit) {
            HeightUnit.CM -> CmPicker(
                heightCm = height,
                onHeightChanged = onHeightChanged
            )
            HeightUnit.FEET_INCHES -> FeetInchesPicker(
                heightCm = height,
                onHeightChanged = onHeightChanged
            )
        }
    }
}

@Composable
private fun UnitSelector(
    selectedUnit: HeightUnit,
    onUnitChanged: (HeightUnit) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(12.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        UnitTab(
            text = "Feet/Inches",
            isSelected = selectedUnit == HeightUnit.FEET_INCHES,
            onClick = { onUnitChanged(HeightUnit.FEET_INCHES) },
            modifier = Modifier.weight(1f)
        )
        UnitTab(
            text = "CM",
            isSelected = selectedUnit == HeightUnit.CM,
            onClick = { onUnitChanged(HeightUnit.CM) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun UnitTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            Color.Transparent
        }
    ) {
        Box(
            modifier = Modifier.padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            )
        }
    }
}

@Composable
private fun CmPicker(
    heightCm: Float?,
    onHeightChanged: (Float) -> Unit
) {
    val minCm = 100
    val maxCm = 250
    val heights = remember { (minCm..maxCm).toList() }

    val initialIndex = remember {
        val value = heightCm?.roundToInt() ?: 170
        (value - minCm).coerceIn(0, heights.lastIndex)
    }

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(listState)
    val coroutineScope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val wheelHeight = screenHeight * 0.35f

    LaunchedEffect(listState.isScrollInProgress) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                if (!listState.isScrollInProgress && index in heights.indices) {
                    onHeightChanged(heights[index].toFloat())
                }
            }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(wheelHeight),
            contentAlignment = Alignment.Center
        ) {
            // Selection lines
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
                Spacer(modifier = Modifier.height(60.dp))
                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            }

            // Fade gradients
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(wheelHeight / 3)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.background,
                                    Color.Transparent
                                )
                            )
                        )
                        .align(Alignment.TopCenter)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(wheelHeight / 3)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.background
                                )
                            )
                        )
                        .align(Alignment.BottomCenter)
                )
            }

            LazyColumn(
                state = listState,
                flingBehavior = flingBehavior,
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = (wheelHeight - 60.dp) / 2)
            ) {
                items(
                    count = heights.size,
                    key = { heights[it] }
                ) { index ->
                    HeightItem(
                        value = "${heights[index]} cm",
                        isSelected = listState.firstVisibleItemIndex == index,
                        onClick = {
                            coroutineScope.launch {
                                listState.animateScrollToItem(index)
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.lg))

        Text(
            text = "${heightCm?.roundToInt() ?: 170} cm",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun FeetInchesPicker(
    heightCm: Float?,
    onHeightChanged: (Float) -> Unit
) {
    val feet = remember { (3..8).toList() }
    val inches = remember { (0..11).toList() }

    // Convert CM to Feet/Inches
    val totalInches = (heightCm ?: 170f) / 2.54f
    val initialFeet = (totalInches / 12).toInt().coerceIn(3, 8)
    val initialInches = (totalInches % 12).toInt().coerceIn(0, 11)

    val feetListState = rememberLazyListState(initialFirstVisibleItemIndex = initialFeet - 3)
    val inchesListState = rememberLazyListState(initialFirstVisibleItemIndex = initialInches)

    val feetFlingBehavior = rememberSnapFlingBehavior(feetListState)
    val inchesFlingBehavior = rememberSnapFlingBehavior(inchesListState)

    val coroutineScope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val wheelHeight = screenHeight * 0.35f

    // Update height when either wheel changes
    LaunchedEffect(feetListState.isScrollInProgress, inchesListState.isScrollInProgress) {
        snapshotFlow {
            feetListState.firstVisibleItemIndex to inchesListState.firstVisibleItemIndex
        }.collect { (feetIndex, inchesIndex) ->
            if (!feetListState.isScrollInProgress && !inchesListState.isScrollInProgress) {
                if (feetIndex in feet.indices && inchesIndex in inches.indices) {
                    val selectedFeet = feet[feetIndex]
                    val selectedInches = inches[inchesIndex]
                    val totalInchesValue = (selectedFeet * 12 + selectedInches).toFloat()
                    val cm = totalInchesValue * 2.54f
                    onHeightChanged(cm)
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Feet Picker
            WheelPicker(
                items = feet,
                listState = feetListState,
                flingBehavior = feetFlingBehavior,
                wheelHeight = wheelHeight,
                suffix = "ft",
                modifier = Modifier.weight(1f),
                onItemClick = { index ->
                    coroutineScope.launch {
                        feetListState.animateScrollToItem(index)
                    }
                }
            )

            // Inches Picker
            WheelPicker(
                items = inches,
                listState = inchesListState,
                flingBehavior = inchesFlingBehavior,
                wheelHeight = wheelHeight,
                suffix = "in",
                modifier = Modifier.weight(1f),
                onItemClick = { index ->
                    coroutineScope.launch {
                        inchesListState.animateScrollToItem(index)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.lg))

        val displayFeet = feet.getOrNull(feetListState.firstVisibleItemIndex) ?: initialFeet
        val displayInches = inches.getOrNull(inchesListState.firstVisibleItemIndex) ?: initialInches

        Text(
            text = "$displayFeet' $displayInches\" (${heightCm?.roundToInt() ?: 170} cm)",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun WheelPicker(
    items: List<Int>,
    listState: androidx.compose.foundation.lazy.LazyListState,
    flingBehavior: androidx.compose.foundation.gestures.FlingBehavior,
    wheelHeight: androidx.compose.ui.unit.Dp,
    suffix: String,
    modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit
) {
    Box(
        modifier = modifier.height(wheelHeight),
        contentAlignment = Alignment.Center
    ) {
        // Selection lines
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(60.dp))
            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
        }

        // Fade gradients
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(wheelHeight / 3)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.background,
                                Color.Transparent
                            )
                        )
                    )
                    .align(Alignment.TopCenter)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(wheelHeight / 3)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
                    .align(Alignment.BottomCenter)
            )
        }

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = (wheelHeight - 60.dp) / 2)
        ) {
            items(
                count = items.size,
                key = { items[it] }
            ) { index ->
                HeightItem(
                    value = "${items[index]} $suffix",
                    isSelected = listState.firstVisibleItemIndex == index,
                    onClick = { onItemClick(index) }
                )
            }
        }
    }
}

@Composable
private fun HeightItem(
    value: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = if (isSelected) 32.sp else 20.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            },
            modifier = Modifier.alpha(if (isSelected) 1f else 0.5f)
        )
    }
}