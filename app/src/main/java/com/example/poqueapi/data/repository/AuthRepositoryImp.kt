package com.example.poqueapi.data.repository


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.poqueapi.data.PreferencesKeys
import com.example.poqueapi.domain.model.LoginResult
import com.example.poqueapi.domain.model.LogoutResult
import com.example.poqueapi.domain.model.User
import com.example.poqueapi.domain.repository.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val store: DataStore<Preferences>,
): AuthRepository {
    override suspend fun login(email: String, password: String): LoginResult {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { firebaseUser ->
                // Guardar estado de sesión
                store.edit { preferences ->
                    preferences[PreferencesKeys.IS_LOGGED_IN] = true
                    preferences[PreferencesKeys.USER_ID] = firebaseUser.uid
                    preferences[PreferencesKeys.USER_EMAIL] = firebaseUser.email ?: ""
                }

                LoginResult.Success(
                    User(
                        id = firebaseUser.uid,
                        email = firebaseUser.email ?: ""
                    )
                )
            } ?: LoginResult.Error("Login failed")
        } catch (e: FirebaseAuthInvalidUserException) {
            LoginResult.Error("User not found")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            LoginResult.Error("Invalid credentials")
        } catch (e: FirebaseNetworkException) {
            Timber.e(e, "Error de red")
            LoginResult.Error("Error de conexión. Verifica tu conexión a internet")
        } catch (e: Exception) {
            LoginResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return store.data.first()[PreferencesKeys.IS_LOGGED_IN] ?: false
    }

    override suspend fun getCurrentUser(): User? {
        val preferences = store.data.first()
        val userId = preferences[PreferencesKeys.USER_ID]
        val userEmail = preferences[PreferencesKeys.USER_EMAIL]

        return if (userId != null && userEmail != null) {
            User(userId, userEmail)
        } else {
            null
        }
    }

    override suspend fun logout(): LogoutResult {
        return try {
            firebaseAuth.signOut()
            // Limpiamos DataStore
            store.edit { preferences ->
                preferences.clear()
            }
            LogoutResult.Success
        } catch (e: Exception) {
            LogoutResult.Error(e.message ?: "Error during logout")
        }
    }
}