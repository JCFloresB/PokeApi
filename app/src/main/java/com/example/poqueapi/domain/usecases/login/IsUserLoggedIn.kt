package com.example.poqueapi.domain.usecases.login

import com.example.poqueapi.domain.model.LoginResult
import com.example.poqueapi.domain.repository.AuthRepository
import javax.inject.Inject

class IsUserLoggedIn @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.isLoggedIn()
    }
}