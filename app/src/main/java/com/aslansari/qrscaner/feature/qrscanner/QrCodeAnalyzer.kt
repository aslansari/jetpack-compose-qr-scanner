package com.aslansari.qrscaner.feature.qrscanner

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

class QrCodeAnalyzer(
    onQrCodeDetected: (String) -> Unit,
): ImageAnalysis.Analyzer {

    override fun analyze(image: ImageProxy) {
        // todo add qr code analyzer implementation
        image.close()
    }
}