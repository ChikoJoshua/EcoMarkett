package com.example.ecomarket.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomarket.data.datastore.UserPreferencesRepository
import com.example.ecomarket.data.db.PurchaseEntity
import com.example.ecomarket.domain.repository.PurchaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AccountUiState(
    val userEmail: String? = null,
    val purchaseCount: Int = 0, // Cantidad de compras (Requerimiento 7)
    val recentPurchases: List<PurchaseEntity> = emptyList(), // Historial resumido (Requerimiento 7)
    val isSessionCleared: Boolean = false,
    val isLoading: Boolean = true
)

class AccountViewModel(
    private val userPrefsRepository: UserPreferencesRepository,
    private val purchaseRepository: PurchaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    init {
        loadAccountData()
    }

    private fun loadAccountData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val email = userPrefsRepository.userEmail.first()

            if (email != null) {
                val count = purchaseRepository.getPurchaseCount(email)

                val recentHistory = purchaseRepository.getPurchaseHistory().first().take(3)

                _uiState.update {
                    it.copy(
                        userEmail = email,
                        purchaseCount = count,
                        recentPurchases = recentHistory,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            userPrefsRepository.clearSession()
            _uiState.update { it.copy(isSessionCleared = true) }
        }
    }
}