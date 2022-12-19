package com.aslansari.qrscaner.feature.qrscanner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun QrScanningScreen(
    viewModel: QrScanViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold { paddingValues ->
        Content(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    uiState: QrScanUIState
) {
    Column {
        Text(text = "QR Scanner")
    }
}