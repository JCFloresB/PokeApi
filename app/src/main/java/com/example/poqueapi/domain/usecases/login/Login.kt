package com.example.poqueapi.domain.usecases.login

import com.example.poqueapi.domain.model.LoginResult
import com.example.poqueapi.domain.repository.AuthRepository
import javax.inject.Inject

class Login @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        if (!isValidEmail(email)) {
            return LoginResult.Error("Invalid email format")
        }
        if (!isValidPassword(password)) {
            return LoginResult.Error("Password must be at least 6 characters")
        }
        return repository.login(email, password)
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}