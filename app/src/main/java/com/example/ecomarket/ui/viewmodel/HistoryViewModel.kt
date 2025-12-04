package com.example.ecomarket.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ecomarket.data.db.PurchaseEntity
import com.example.ecomarket.domain.repository.PurchaseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope

data class HistoryUiState(
    val purchases: List<PurchaseEntity> = emptyList(),
    val isLoading: Boolean = true
)

class HistoryViewModel(
    purchaseRepository: PurchaseRepository
) : ViewModel() {

    val uiState: StateFlow<HistoryUiState> = purchaseRepository.getPurchaseHistory()
        .map { purchases ->
            HistoryUiState(
                purchases = purchases,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HistoryUiState()
        )
}