package com.example.responsiveapp.presentation.barcode

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.io.Closeable

// CHANGED:
//  * only product barcodes (EAN-13/EAN-8/UPC-A/UPC-E) are decoded. Before, every format was
//    enabled, so a QR code or any other label on a package triggered a food lookup that
//    could never match. To go back to everything, use BarcodeScanning.getClient().
//  * the same value is reported at most once per REPEAT_WINDOW_MS (a barcode sits in view for
//    dozens of frames).
//  * implements Closeable so the ML Kit scanner is released with the screen.
class BarcodeAnalyzer(
    private val onBarcodeDetected: (String) -> Unit,
) : ImageAnalysis.Analyzer, Closeable {

    private val scanner: BarcodeScanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_EAN_8,
                Barcode.FORMAT_UPC_A,
                Barcode.FORMAT_UPC_E,
            )
            .build()
    )

    private var lastValue: String? = null
    private var lastReportedAt = 0L

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                val value = barcodes.firstNotNullOfOrNull { it.rawValue } ?: return@addOnSuccessListener
                val now = System.currentTimeMillis()
                if (value != lastValue || now - lastReportedAt > REPEAT_WINDOW_MS) {
                    lastValue = value
                    lastReportedAt = now
                    onBarcodeDetected(value)
                }
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    override fun close() {
        scanner.close()
    }

    private companion object {
        const val REPEAT_WINDOW_MS = 1_500L
    }
}

// CHANGED: FatSecret looks products up by 13-digit GTIN, but ML Kit returns UPC-A as 12
// digits and EAN-8 as 8, so shorter codes were sent as-is and could not match. Pads with
// leading zeros (the standard GTIN-8/12 -> GTIN-13 conversion). Returns null for anything
// that is not a plain 8-13 digit code.
internal fun normalizeBarcode(raw: String): String? {
    val digits = raw.trim()
    if (digits.length !in 8..13 || !digits.all { it in '0'..'9' }) return null
    return digits.padStart(13, '0')
}
