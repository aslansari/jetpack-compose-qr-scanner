package com.aslansari.qrscaner.feature.qrscanner

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class QrScanViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState: MutableStateFlow<QrScanUIState> = MutableStateFlow(QrScanUIState())
    val uiState: StateFlow<QrScanUIState> = _uiState

}

data class QrScanUIState(
    val loading: Boolean = false
)