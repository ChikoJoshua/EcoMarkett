package com.example.ecomarket.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomarket.data.datastore.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isValid: Boolean = true,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false
)

class LoginViewModel(
    private val userPrefsRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun setEmail(newEmail: String) {
        _uiState.update {
            it.copy(
                email = newEmail,
                isValid = true
            )
        }
    }

    fun setPassword(newPassword: String) {
        _uiState.update {
            it.copy(password = newPassword)
        }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val email = _uiState.value.email
            val password = _uiState.value.password

            val isValidFormat = email.contains("@") && email.contains(".") && password.isNotEmpty()

            if (isValidFormat) {
                userPrefsRepository.setLoggedIn(email)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginSuccess = true
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isValid = false,
                        isLoading = false
                    )
                }
            }
        }
    }
}
