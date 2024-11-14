package com.example.poqueapi.domain.model

sealed class LogoutResult {
    data object Success: LogoutResult()
    data class Error(val message: String): LogoutResult()
}