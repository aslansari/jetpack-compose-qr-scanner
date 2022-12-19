package com.aslansari.qrscaner.feature.qrscanner

import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

@ExperimentalGetImage class QrCodeAnalyzer(
    private val onQrCodeDetected: (String) -> Unit,
): ImageAnalysis.Analyzer {

    private val scannerOptions = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()
    private val scanner: BarcodeScanner = BarcodeScanning.getClient(scannerOptions)

    override fun analyze(image: ImageProxy) {
        if (image.image != null) {
            val inputImage = InputImage.fromMediaImage(image.image!!, image.imageInfo.rotationDegrees)
            scanner.process(inputImage)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        it.result.let { barcodes ->
                            barcodes.forEach { barcode ->
                                barcode.rawValue?.let(onQrCodeDetected)
                            }
                        }
                    } else {
                        it.exception?.let { exception ->
                            Log.e("QR Analyzer", exception.message.orEmpty())
                        }
                    }
                    image.close()
                }
        } else {
            image.close()
        }
    }
}