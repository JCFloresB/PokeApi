package com.example.poqueapi.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poqueapi.domain.model.LoginResult
import com.example.poqueapi.domain.usecases.login.IsUserLoggedIn
import com.example.poqueapi.domain.usecases.login.Login
import com.example.poqueapi.domain.usecases.login.Logout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: Login,
    private val logoutUseCase: Logout,
    private val isLoggedInUC: IsUserLoggedIn,
): ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    // Estado global de autenticación
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            _authState.value = if (isLoggedInUC()) {
                AuthState.Authenticated
            } else {
                AuthState.Unauthenticated
            }
        }
    }

    fun onEmailChanged(email: String) {
        _loginState.update { it.copy(
            email = email,
            isEmailError = false,
            emailErrorMessage = ""
        ) }
    }

    fun onPasswordChanged(password: String) {
        _loginState.update { it.copy(
            password = password,
            isPasswordError = false,
            passwordErrorMessage = ""
        ) }
    }

    fun login() {
        viewModelScope.launch {
            _loginState.update { it.copy(isLoading = true, error = null) }

            try {
                val result = loginUseCase(loginState.value.email, loginState.value.password)
                when (result) {
                    is LoginResult.Success -> {
                        _loginState.update { it.copy(
                            isLoading = false,
                            isSuccess = true
                        ) }
                        _authState.value = AuthState.Authenticated
                    }
                    is LoginResult.Error -> {
                        _loginState.update { it.copy(
                            isLoading = false,
                            error = result.message
                        ) }
                    }
                }
            } catch (e: Exception) {
                _loginState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                ) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                logoutUseCase()
                _authState.value = AuthState.Unauthenticated
                // Resetear el estado del login
                _loginState.value = LoginState()
            } catch (e: Exception) {
                _loginState.update { it.copy(
                    error = e.message ?: "Error during logout"
                ) }
            }
        }
    }

}