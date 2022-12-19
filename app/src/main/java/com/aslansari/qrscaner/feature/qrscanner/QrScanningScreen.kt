@file:OptIn(ExperimentalPermissionsApi::class)

package com.aslansari.qrscaner.feature.qrscanner

import android.Manifest
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.aslansari.qrscaner.feature.permission.FeatureThatRequiresCameraPermission
import com.aslansari.qrscaner.feature.permission.NeedCameraPermissionScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.withContext

typealias AndroidSize = android.util.Size

@Composable
@ExperimentalGetImage
fun QrScanningScreen(
    viewModel: QrScanViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    cameraPermissionState.launchPermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }
    val preview = Preview.Builder().build()
    val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
        .setTargetResolution(
            AndroidSize(previewView.width, previewView.height)
        )
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
        .also {
            it.setAnalyzer(
                Dispatchers.Default.asExecutor(),
                QrCodeAnalyzer { result ->
                    viewModel.onQrCodeDetected(result)
                }
            )
        }

    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(uiState.lensFacing)
        .build()
    var camera by remember { mutableStateOf<Camera?>(null) }

    LaunchedEffect(uiState.lensFacing) {
        val cameraProvider = ProcessCameraProvider.getInstance(context)
        camera = withContext(Dispatchers.IO) {
            cameraProvider.get()
        }.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    FeatureThatRequiresCameraPermission(
        deniedContent = { status ->
            NeedCameraPermissionScreen(
                requestPermission = cameraPermissionState::launchPermissionRequest,
                shouldShowRationale = status.shouldShowRationale
            )
        },
        grantedContent = {
            Scaffold { paddingValues ->
                Content(
                    modifier = Modifier.padding(paddingValues),
                    uiState = uiState,
                    previewView = previewView,
                )
            }
        }
    )
}


@Composable
private fun Content(
    modifier: Modifier,
    previewView: PreviewView,
    uiState: QrScanUIState
) {
    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            factory = {
                previewView
            }
        )
        val offsetInPx: Float
        val widthInPx: Float
        val heightInPx: Float
        with(LocalDensity.current) {
            offsetInPx = 300.dp.toPx()
            widthInPx = 250.dp.toPx()
            heightInPx = 250.dp.toPx()
        }
        Box(modifier = Modifier
            .fillMaxSize().drawBehind {
                with(drawContext.canvas.nativeCanvas) {
                    val canvasWidth = size.width

                    with(drawContext.canvas.nativeCanvas) {
                        val checkPoint = saveLayer(null, null)

                        // Destination
                        drawRect(Color.Black.copy(alpha = .5f))

                        // Source
                        drawRoundRect(
                            topLeft = Offset(
                                x = (canvasWidth - widthInPx) / 2,
                                y = offsetInPx
                            ),
                            size = Size(widthInPx, heightInPx),
                            cornerRadius = CornerRadius(30f,30f),
                            color = Color.Transparent,
                            blendMode = BlendMode.Clear
                        )
                        restoreToCount(checkPoint)
                    }
                }
        })
    }
}