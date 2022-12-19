@file:OptIn(ExperimentalAnimationApi::class)

package com.aslansari.qrscaner.feature.permission

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FeatureThatRequiresCameraPermission(
    deniedContent: @Composable (PermissionStatus.Denied) -> Unit,
    grantedContent: @Composable () -> Unit
) {

    // Camera permission state
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    val status = cameraPermissionState.status
    AnimatedContent(targetState = status) { permissionStatus ->
        when(permissionStatus) {
            is PermissionStatus.Granted -> grantedContent()
            is PermissionStatus.Denied -> deniedContent(permissionStatus)
        }
    }
}
