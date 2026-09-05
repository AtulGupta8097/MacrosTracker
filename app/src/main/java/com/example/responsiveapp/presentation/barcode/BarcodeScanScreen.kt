package com.example.responsiveapp.presentation.barcode

import android.Manifest
import android.content.pm.PackageManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.responsiveapp.presentation.ui.theme.CarbsColor
import com.example.responsiveapp.presentation.ui.theme.FatColor
import com.example.responsiveapp.presentation.ui.theme.ProteinColor
import com.example.responsiveapp.presentation.ui.theme.spacing
import java.util.concurrent.Executors
import kotlin.math.roundToInt

@Composable
fun BarcodeScanScreen(
    onBack: () -> Unit,
    onLogged: () -> Unit,
    viewModel: BarcodeScanViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        if (uiState is BarcodeScanUiState.Added) onLogged()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(
            enabled = uiState is BarcodeScanUiState.Scanning,
            onBarcodeDetected = viewModel::onBarcodeDetected,
            modifier = Modifier.fillMaxSize(),
        )

        ScanOverlay(modifier = Modifier.fillMaxSize())

        TopBar(
            onBack = onBack,
            modifier = Modifier.fillMaxWidth(),
        )

        ResultCard(
            uiState = uiState,
            onServingSelected = viewModel::onServingSelected,
            onQuantityChanged = viewModel::onQuantityChanged,
            onAddToLog = viewModel::onAddToLog,
            onScanAgain = viewModel::onScanAgain,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun CameraPreview(
    enabled: Boolean,
    onBarcodeDetected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    if (!hasPermission) return

    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }
    DisposableEffect(Unit) { onDispose { analysisExecutor.shutdown() } }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val analysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(analysisExecutor, BarcodeAnalyzer(onBarcodeDetected))
                    }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analysis,
                )
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
    )
}

@Composable
private fun ScanOverlay(modifier: Modifier = Modifier) {
    val accentColor = MaterialTheme.colorScheme.primary
    val infiniteTransition = rememberInfiniteTransition(label = "scanLine")
    val lineProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "scanLineProgress",
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .width(250.dp)
                .height(150.dp),
        ) {
            CornerBracket(Modifier.align(Alignment.TopStart), accentColor, top = true, start = true)
            CornerBracket(Modifier.align(Alignment.TopEnd), accentColor, top = true, start = false)
            CornerBracket(Modifier.align(Alignment.BottomStart), accentColor, top = false, start = true)
            CornerBracket(Modifier.align(Alignment.BottomEnd), accentColor, top = false, start = false)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .align(Alignment.TopStart)
                    .padding(top = (148.dp * lineProgress))
                    .background(accentColor),
            )
        }

        Text(
            text = "Position barcode within the frame",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = MaterialTheme.spacing.xl)
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(50))
                .padding(horizontal = MaterialTheme.spacing.md, vertical = MaterialTheme.spacing.sm),
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun CornerBracket(modifier: Modifier, color: Color, top: Boolean, start: Boolean) {
    Box(
        modifier = modifier
            .size(32.dp)
            .background(Color.Transparent),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .align(if (top) Alignment.TopStart else Alignment.BottomStart)
                .background(color),
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(3.dp)
                .align(if (start) Alignment.TopStart else Alignment.TopEnd)
                .background(color),
        )
    }
}

@Composable
private fun TopBar(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(MaterialTheme.spacing.md),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
        }
        Text("Scan barcode", color = Color.White, fontWeight = FontWeight.Medium)
        Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.White)
    }
}

@Composable
private fun ResultCard(
    uiState: BarcodeScanUiState,
    onServingSelected: (com.example.responsiveapp.domain.model.Serving) -> Unit,
    onQuantityChanged: (Float) -> Unit,
    onAddToLog: () -> Unit,
    onScanAgain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is BarcodeScanUiState.Scanning, is BarcodeScanUiState.Added -> Unit

        is BarcodeScanUiState.Loading -> Surface(
            modifier = modifier,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Row(
                modifier = Modifier.padding(MaterialTheme.spacing.lg),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    strokeWidth = 3.dp,
                    color = MaterialTheme.colorScheme.primary,
                )
                Column(modifier = Modifier.padding(start = MaterialTheme.spacing.md)) {
                    Text("Looking up product", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                    Text(
                        "Fetching nutrition from FatSecret",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        is BarcodeScanUiState.NotFound -> EmptyResultCard(
            title = "No match found",
            message = "This barcode isn't in FatSecret's database yet.",
            onScanAgain = onScanAgain,
            modifier = modifier,
        )

        is BarcodeScanUiState.Error -> EmptyResultCard(
            title = "Something went wrong",
            message = uiState.message,
            onScanAgain = onScanAgain,
            modifier = modifier,
        )

        is BarcodeScanUiState.Found -> FoundResultCard(
            state = uiState,
            onQuantityChanged = onQuantityChanged,
            onAddToLog = onAddToLog,
            onScanAgain = onScanAgain,
            modifier = modifier,
        )
    }
}

@Composable
private fun EmptyResultCard(
    title: String,
    message: String,
    onScanAgain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(title, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            Text(
                message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = MaterialTheme.spacing.xxs),
            )
            Button(
                onClick = onScanAgain,
                modifier = Modifier.padding(top = MaterialTheme.spacing.md),
                shape = RoundedCornerShape(50),
            ) {
                Text("Scan again")
            }
        }
    }
}

@Composable
private fun FoundResultCard(
    state: BarcodeScanUiState.Found,
    onQuantityChanged: (Float) -> Unit,
    onAddToLog: () -> Unit,
    onScanAgain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        shadowElevation = 12.dp,
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.lg)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        state.foodName,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                    )
                    Text(
                        listOfNotNull(state.foodBrand, state.selectedServing.description).joinToString(" · "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.md)
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
                    .padding(MaterialTheme.spacing.md),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Servings", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onQuantityChanged(state.quantity - 0.5f) }) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                    }
                    Text(
                        "${state.quantity}",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(32.dp),
                    )
                    IconButton(onClick = { onQuantityChanged(state.quantity + 0.5f) }) {
                        Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                }
            }

            Text(
                "${state.scaledNutrition.calories.roundToInt()} kcal",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = MaterialTheme.spacing.md),
                color = MaterialTheme.colorScheme.onSurface,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.md),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
            ) {
                MacroChip("Protein", state.scaledNutrition.protein, ProteinColor, Modifier.weight(1f))
                MacroChip("Carbs", state.scaledNutrition.carbs, CarbsColor, Modifier.weight(1f))
                MacroChip("Fat", state.scaledNutrition.fat, FatColor, Modifier.weight(1f))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.lg),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
            ) {
                androidx.compose.material3.OutlinedButton(
                    onClick = onScanAgain,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Text("Scan again")
                }
                Button(
                    onClick = onAddToLog,
                    enabled = !state.isAdding,
                    modifier = Modifier.weight(2f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                ) {
                    if (state.isAdding) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Add to log", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun MacroChip(label: String, grams: Float, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.spacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(label.uppercase(), color = color, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelSmall)
        Text("${grams.roundToInt()}g", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}