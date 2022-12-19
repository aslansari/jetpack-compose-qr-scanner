package com.aslansari.qrscaner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.aslansari.qrscaner.feature.qrscanner.QrScanningScreen
import com.aslansari.qrscaner.ui.theme.QrScanerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QrScanerTheme {
                // A surface container using the 'background' color from the theme
                QrScanningScreen(
                    viewModel = hiltViewModel()
                )
            }
        }
    }
}
