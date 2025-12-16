package com.example.ecomarket.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ecomarket.data.datastore.UserPreferencesRepository
import com.example.ecomarket.domain.repository.PurchaseRepository
import com.example.ecomarket.data.db.PurchaseEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HistoryUiState(
    val purchases: List<PurchaseEntity> = emptyList(),
    val isLoading: Boolean = true
)

class HistoryViewModel(
    private val context: Context,
    private val purchaseRepository: PurchaseRepository,
    private val userPrefsRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val email = userPrefsRepository.getUserEmail()
            val purchases = purchaseRepository.getPurchaseHistory()
                .collect { list ->
                    _uiState.value = HistoryUiState(purchases = list, isLoading = false)
                }
        }
    }
}

class HistoryViewModelFactory(
    private val context: Context,
    private val purchaseRepository: PurchaseRepository,
    private val userPrefsRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(context, purchaseRepository, userPrefsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
