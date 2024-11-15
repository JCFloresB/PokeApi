package com.example.poqueapi.domain.model

sealed class LoginResult {
    data class Success(val user: User): LoginResult()
    data class Error(val message: String): LoginResult()
}