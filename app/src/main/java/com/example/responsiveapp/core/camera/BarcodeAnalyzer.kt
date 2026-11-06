package com.example.responsiveapp.core.camera

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.atomic.AtomicBoolean

class BarcodeAnalyzer(
    private val onBarcodeDetected: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_EAN_8,
                Barcode.FORMAT_UPC_A
            )
            .build()
    )

    private val isProcessing = AtomicBoolean(false)

    @Volatile
    private var isEnabled = true

    fun pause() {
        isEnabled = false
    }

    fun resume() {
        isEnabled = true
    }

    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
    }

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image

        if (!isEnabled || mediaImage == null || !isProcessing.compareAndSet(false, true)) {
            imageProxy.close()
            return
        }

        try {
            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    val value = barcodes.firstNotNullOfOrNull { it.rawValue }
                    if (isEnabled && value != null) {
                        onBarcodeDetected(value)
                    }
                }
                .addOnCompleteListener {
                    isProcessing.set(false)
                    imageProxy.close()
                }
        } catch (_: Exception) {
            isProcessing.set(false)
            imageProxy.close()
        }
    }

    fun close() {
        isEnabled = false
        scanner.close()
    }
}
