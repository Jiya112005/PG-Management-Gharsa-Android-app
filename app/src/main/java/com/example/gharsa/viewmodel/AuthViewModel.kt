package com.example.gharsa.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gharsa.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class UserRole {
    RESIDENT, MANAGER
}

data class Ticket(
    val id: Long,
    val residentEmail: String,
    val title: String,
    val description: String,
    var status: String,
    val date: String
)

data class Rent(
    val id: Long,
    val residentEmail: String,
    val amount: String,
    var status: String,
    val dueDate: String,
    val month: String
)

class AuthViewModel : ViewModel() {
    var userRole by mutableStateOf(UserRole.RESIDENT)
        private set

    var loginId by mutableStateOf("sneha.roy@example.com")
    var password by mutableStateOf("pgSuperSecure99!")
    var mobileNumber by mutableStateOf("")
    var passwordVisible by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isLoggedIn by mutableStateOf(false)
        private set

    var toastMessage by mutableStateOf<String?>(null)
        private set

    var loginError by mutableStateOf<String?>(null)
        private set

    var currentUserName by mutableStateOf("Sneha Roy")
        private set

    var currentRoomNumber by mutableStateOf("304")
        private set

    var currentUserEmail by mutableStateOf("sneha.roy@example.com")
        private set

    var currentUserMobile by mutableStateOf("")
        private set

    var tickets by mutableStateOf<List<Ticket>>(emptyList())
        private set

    var rents by mutableStateOf<List<Rent>>(emptyList())
        private set

    fun selectRole(role: UserRole) {
        userRole = role
        loginError = null
        if (role == UserRole.RESIDENT) {
            loginId = "sneha.roy@example.com"
            password = "pgSuperSecure99!"
        } else {
            loginId = "manager.rajesh@gharsa.in"
            password = "admin123"
        }
    }

    fun updateLoginId(id: String) {
        loginId = id
        loginError = null
    }

    fun updatePassword(pass: String) {
        password = pass
        loginError = null
    }

    fun updateMobileNumber(mobile: String) {
        mobileNumber = mobile
        loginError = null
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
        if (loginId.isBlank() || password.isBlank()) {
            loginError = "Please enter both email and password."
            return
        }

        viewModelScope.launch {
            isLoading = true
            loginError = null
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.login(LoginRequest(loginId.trim(), password))
                }
                isLoading = false
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    isLoggedIn = true
                    currentUserName = user.name
                    currentRoomNumber = user.roomNumber
                    currentUserEmail = user.email
                    currentUserMobile = user.mobile ?: ""
                    userRole = if (user.role == "MANAGER") UserRole.MANAGER else UserRole.RESIDENT
                    loadUserData()
                    showToast("Connected to MySQL backend! Welcome, ${user.name}")
                } else if (response.code() == 401 || response.code() == 404) {
                    loginError = "User not present"
                } else {
                    loginError = "Login failed (Server error: ${response.code()})"
                }
            } catch (e: Exception) {
                isLoading = false
                loginError = "User not present (or MySQL server offline at http://127.0.0.1:3000)"
            }
        }
    }

    fun sendOtp(mobile: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        if (mobile.isBlank() || mobile.length < 10) {
            onError("Please enter a valid 10-digit mobile number.")
            return
        }

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.sendOtp(SendOtpRequest(mobile))
                }
                if (response.isSuccessful && response.body() != null) {
                    val demoOtp = response.body()!!.demoOtp ?: "8421"
                    onSuccess(demoOtp)
                } else {
                    onError("User not found or mobile number not registered.")
                }
            } catch (e: Exception) {
                onError("User not found or mobile number not registered (or server offline)")
            }
        }
    }

    fun verifyOtpAndLogin(mobile: String, otp: String, onError: (String) -> Unit) {
        if (otp.isBlank() || otp.length < 4) {
            onError("Please enter the 4-digit OTP.")
            return
        }

        viewModelScope.launch {
            isLoading = true
            loginError = null
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.verifyOtp(VerifyOtpRequest(mobile, otp))
                }
                isLoading = false
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    isLoggedIn = true
                    currentUserName = user.name
                    currentRoomNumber = user.roomNumber
                    currentUserEmail = user.email
                    currentUserMobile = user.mobile ?: mobile
                    userRole = if (user.role == "MANAGER") UserRole.MANAGER else UserRole.RESIDENT
                    loadUserData()
                    showToast("OTP Verified! Welcome, ${user.name}")
                } else {
                    onError("Invalid or expired OTP.")
                }
            } catch (e: Exception) {
                isLoading = false
                onError("Connection error verifying OTP: ${e.localizedMessage}")
            }
        }
    }

    fun signUp(
        name: String,
        email: String,
        password: String,
        role: String,
        roomNumber: String,
        mobile: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.signUp(SignUpRequest(name, email.trim().lowercase(), password, role, roomNumber, mobile))
                }
                if (response.isSuccessful) {
                    showToast("Account registered successfully in MySQL database!")
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Server error"
                    onError("Registration failed: $errorBody")
                }
            } catch (e: Exception) {
                onError("MySQL Connection Error: ${e.localizedMessage ?: "Unable to reach server"}")
            }
        }
    }

    fun updateProfile(
        name: String,
        mobile: String,
        roomNumber: String,
        pass: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.updateProfile(
                        UpdateProfileRequest(
                            email = currentUserEmail,
                            name = name,
                            mobile = mobile,
                            roomNumber = roomNumber,
                            password = pass
                        )
                    )
                }
                if (response.isSuccessful && response.body() != null) {
                    val updated = response.body()!!
                    currentUserName = updated.name
                    currentUserMobile = updated.mobile ?: mobile
                    currentRoomNumber = updated.roomNumber
                    showToast("Profile updated successfully in MySQL!")
                    onSuccess()
                } else {
                    onError("Failed to update profile on server.")
                }
            } catch (e: Exception) {
                onError("Connection error updating profile: ${e.localizedMessage}")
            }
        }
    }

    fun loadUserData() {
        viewModelScope.launch {
            try {
                val tResp = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getTickets(currentUserEmail)
                }
                if (tResp.isSuccessful && tResp.body() != null) {
                    tickets = tResp.body()!!.map { Ticket(it.id, it.residentEmail, it.title, it.description, it.status, it.date) }
                }

                val rResp = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getRents(currentUserEmail)
                }
                if (rResp.isSuccessful && rResp.body() != null) {
                    rents = rResp.body()!!.map { Rent(it.id, it.residentEmail, it.amount, it.status, it.dueDate, it.month) }
                }
            } catch (e: Exception) {
                showToast("Failed to load live database records.")
            }
        }
    }

    fun raiseTicket(title: String, description: String) {
        if (title.isBlank() || description.isBlank()) {
            showToast("Title and description cannot be empty.")
            return
        }

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.createTicket(CreateTicketRequest(currentUserEmail, title, description, "Today"))
                }
                loadUserData()
                showToast("Maintenance ticket saved to MySQL database!")
            } catch (e: Exception) {
                showToast("Failed to save ticket to MySQL: ${e.localizedMessage}")
            }
        }
    }

    fun payRent(rentId: Long) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.payRent(PayRentRequest(rentId))
                }
                loadUserData()
                showToast("Rent payment updated in MySQL database!")
            } catch (e: Exception) {
                showToast("Failed to update rent in MySQL: ${e.localizedMessage}")
            }
        }
    }

    fun logout() {
        isLoggedIn = false
        loginError = null
        showToast("Logged out successfully")
    }
}
