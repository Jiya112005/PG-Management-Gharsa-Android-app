package com.example.gharsa.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class UserRole {
    RESIDENT, MANAGER
}

class AuthViewModel : ViewModel() {
    var userRole by mutableStateOf(UserRole.RESIDENT)
        private set

    var loginId by mutableStateOf("sneha.roy@example.com")
        var s: String get() = loginId; set(value) { loginId = value }

    var password by mutableStateOf("pgSuperSecure99!")
    var passwordVisible by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isLoggedIn by mutableStateOf(false)
        private set

    var toastMessage by mutableStateOf<String?>(null)
        private set

    fun selectRole(role: UserRole) {
        userRole = role
        if (role == UserRole.RESIDENT) {
            loginId = "sneha.roy@example.com"
            showToast("Switched to Resident Portal mode")
        } else {
            loginId = "manager.rajesh@gharsa.in"
            showToast("Switched to Property Owner / Manager mode")
        }
    }

    fun updateLoginId(id: String) {
        loginId = id
    }

    fun updatePassword(pass: String) {
        password = pass
    }

    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }

    fun showToast(msg: String) {
        toastMessage = msg
    }

    fun clearToast() {
        toastMessage = null
    }

    fun login() {
        viewModelScope.launch {
            isLoading = true
            showToast("Verifying Credentials...")
            delay(1200)
            isLoading = false
            isLoggedIn = true
            showToast("Welcome back! Opening Dashboard...")
        }
    }

    fun loginWithOtp() {
        showToast("OTP code 8421 sent to registered mobile ending with ••89")
    }

    fun biometricLogin() {
        showToast("Waiting for native Android Biometric Prompt...")
    }

    fun logout() {
        isLoggedIn = false
        showToast("Logged out successfully")
    }
}
