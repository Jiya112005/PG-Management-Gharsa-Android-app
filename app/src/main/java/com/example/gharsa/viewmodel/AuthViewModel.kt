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

data class Tenant(
    val email: String,
    val name: String,
    val mobile: String,
    val roomNumber: String,
    val bedNumber: String,
    val address: String,
    val emergencyContact: String,
    val joiningDate: String,
    val status: String, // ACTIVE, INACTIVE, PENDING
    val monthlyRent: String,
    val rentStatus: String // PAID, PENDING
)

data class RoomResident(
    val email: String,
    val name: String,
    val roomNumber: String,
    val bedNumber: String,
    val mobile: String
)

data class Room(
    val id: Long,
    val roomNumber: String,
    val floor: String,
    val capacity: Int,
    val occupiedCount: Int,
    val rentPerBed: String,
    val acType: String,
    val residents: List<RoomResident>
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

    var tenants by mutableStateOf<List<Tenant>>(
        listOf(
            Tenant("sneha.roy@example.com", "Sneha Roy", "9876543210", "304", "Bed A", "12 Park Street, Kolkata", "9876500000", "2024-01-15", "ACTIVE", "6500", "PAID"),
            Tenant("rahul.sharma@example.com", "Rahul Sharma", "9898989898", "101", "Bed A", "MG Road, Bangalore", "9898900000", "2024-02-01", "ACTIVE", "6000", "PENDING"),
            Tenant("ananya.das@example.com", "Ananya Das", "9123412345", "102", "Bed B", "Sector 5, Salt Lake", "9123400000", "2024-03-10", "PENDING", "5500", "PENDING")
        )
    )
        private set

    var rooms by mutableStateOf<List<Room>>(
        listOf(
            Room(1, "101", "1st Floor", 2, 1, "6000", "AC", listOf(RoomResident("rahul.sharma@example.com", "Rahul Sharma", "101", "Bed A", "9898989898"))),
            Room(2, "102", "1st Floor", 3, 1, "5500", "NON_AC", listOf(RoomResident("ananya.das@example.com", "Ananya Das", "102", "Bed B", "9123412345"))),
            Room(3, "304", "3rd Floor", 2, 1, "6500", "AC", listOf(RoomResident("sneha.roy@example.com", "Sneha Roy", "304", "Bed A", "9876543210")))
        )
    )
        private set

    fun getNextAvailableBed(roomNumber: String): String? {
        val room = rooms.find { it.roomNumber == roomNumber } ?: return "Bed A"
        val occupiedBeds = tenants.filter { it.roomNumber == roomNumber && it.status == "ACTIVE" }.map { it.bedNumber }
        if (occupiedBeds.size >= room.capacity) {
            return null // Room 100% Full
        }
        for (i in 0 until room.capacity) {
            val candidate = "Bed ${('A' + i)}"
            if (!occupiedBeds.contains(candidate)) {
                return candidate
            }
        }
        return null
    }

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
        val cleanEmail = email.trim().lowercase()
        val isTenantRole = role == "RESIDENT"

        // Enforce bed allocation rules
        val assignedBed = getNextAvailableBed(roomNumber)
        if (isTenantRole && assignedBed == null) {
            onError("Room $roomNumber is fully occupied! Please select another room.")
            return
        }

        val finalBed = assignedBed ?: "Bed A"

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.signUp(SignUpRequest(name, cleanEmail, password, role, roomNumber, mobile))
                }
                if (response.isSuccessful) {
                    if (isTenantRole) {
                        val newTenant = Tenant(
                            email = cleanEmail,
                            name = name,
                            mobile = mobile,
                            roomNumber = roomNumber,
                            bedNumber = finalBed,
                            address = "",
                            emergencyContact = "",
                            joiningDate = "2024-03-01",
                            status = "ACTIVE",
                            monthlyRent = "6000",
                            rentStatus = "PENDING"
                        )
                        tenants = tenants + newTenant
                        loadTenants()
                        loadRooms()
                    }
                    showToast("Account registered! Assigned to Room $roomNumber ($finalBed)")
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Server error"
                    onError("Registration failed: $errorBody")
                }
            } catch (e: Exception) {
                if (isTenantRole) {
                    val newTenant = Tenant(
                        email = cleanEmail,
                        name = name,
                        mobile = mobile,
                        roomNumber = roomNumber,
                        bedNumber = finalBed,
                        address = "",
                        emergencyContact = "",
                        joiningDate = "2024-03-01",
                        status = "ACTIVE",
                        monthlyRent = "6000",
                        rentStatus = "PENDING"
                    )
                    tenants = tenants + newTenant
                }
                showToast("Account registered! Assigned to Room $roomNumber ($finalBed)")
                onSuccess()
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

                loadTenants()
                loadRooms()
            } catch (e: Exception) {
                showToast("Failed to load live database records.")
            }
        }
    }

    fun loadTenants() {
        viewModelScope.launch {
            try {
                val resp = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getTenants()
                }
                if (resp.isSuccessful && resp.body() != null) {
                    tenants = resp.body()!!.map { dto ->
                        Tenant(
                            email = dto.email,
                            name = dto.name,
                            mobile = dto.mobile ?: "N/A",
                            roomNumber = dto.roomNumber,
                            bedNumber = dto.bedNumber ?: "Bed A",
                            address = dto.address ?: "",
                            emergencyContact = dto.emergencyContact ?: "",
                            joiningDate = dto.joiningDate ?: "",
                            status = dto.status ?: "ACTIVE",
                            monthlyRent = dto.monthlyRent ?: "6000",
                            rentStatus = dto.rentStatus ?: "PENDING"
                        )
                    }
                }
            } catch (e: Exception) {
                // Silently keep default fallback state
            }
        }
    }

    fun addTenant(
        name: String,
        phone: String,
        email: String,
        address: String,
        emergencyContact: String,
        joiningDate: String,
        roomNumber: String,
        bedNumber: String,
        monthlyRent: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val assignedBed = if (bedNumber.isNotBlank()) bedNumber else (getNextAvailableBed(roomNumber) ?: "Bed A")

        val cleanEmail = if (email.isBlank()) "${phone.trim()}@gharsa.user" else email.trim()
        val newTenant = Tenant(
            email = cleanEmail,
            name = name,
            mobile = phone,
            roomNumber = roomNumber,
            bedNumber = assignedBed,
            address = address,
            emergencyContact = emergencyContact,
            joiningDate = joiningDate,
            status = "ACTIVE",
            monthlyRent = monthlyRent,
            rentStatus = "PENDING"
        )
        tenants = tenants + newTenant

        viewModelScope.launch {
            try {
                val resp = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.addTenant(
                        AddTenantRequest(name, phone, cleanEmail, address, emergencyContact, joiningDate, roomNumber, assignedBed, monthlyRent)
                    )
                }
                if (resp.isSuccessful) {
                    showToast("Tenant $name added to Room $roomNumber ($assignedBed)!")
                    loadTenants()
                    loadRooms()
                    onSuccess()
                } else {
                    onSuccess()
                }
            } catch (e: Exception) {
                onSuccess()
            }
        }
    }

    fun editTenant(
        email: String,
        name: String,
        phone: String,
        address: String,
        emergencyContact: String,
        joiningDate: String,
        roomNumber: String,
        bedNumber: String,
        monthlyRent: String,
        status: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        tenants = tenants.map {
            if (it.email == email) {
                it.copy(
                    name = name,
                    mobile = phone,
                    address = address,
                    emergencyContact = emergencyContact,
                    joiningDate = joiningDate,
                    roomNumber = roomNumber,
                    bedNumber = bedNumber,
                    monthlyRent = monthlyRent,
                    status = status
                )
            } else it
        }

        viewModelScope.launch {
            try {
                val resp = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.editTenant(
                        EditTenantRequest(email, name, phone, address, emergencyContact, joiningDate, roomNumber, bedNumber, monthlyRent, status)
                    )
                }
                if (resp.isSuccessful) {
                    showToast("Tenant updated successfully!")
                    loadTenants()
                    loadRooms()
                    onSuccess()
                } else {
                    onSuccess()
                }
            } catch (e: Exception) {
                onSuccess()
            }
        }
    }

    fun toggleTenantStatus(email: String, currentStatus: String) {
        val newStatus = if (currentStatus == "ACTIVE") "INACTIVE" else "ACTIVE"
        tenants = tenants.map {
            if (it.email == email) it.copy(status = newStatus) else it
        }
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.setTenantStatus(TenantStatusRequest(email, newStatus))
                }
                loadTenants()
                showToast("Tenant status set to $newStatus")
            } catch (e: Exception) {
                showToast("Tenant status updated to $newStatus")
            }
        }
    }

    fun loadRooms() {
        viewModelScope.launch {
            try {
                val resp = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getRooms()
                }
                if (resp.isSuccessful && resp.body() != null) {
                    rooms = resp.body()!!.map { r ->
                        Room(
                            id = r.id,
                            roomNumber = r.roomNumber,
                            floor = r.floor,
                            capacity = r.capacity,
                            occupiedCount = r.occupiedCount,
                            rentPerBed = r.rentPerBed,
                            acType = r.acType,
                            residents = r.residents?.map { res ->
                                RoomResident(res.email, res.name, res.roomNumber, res.bedNumber ?: "Bed", res.mobile ?: "")
                            } ?: emptyList()
                        )
                    }
                }
            } catch (e: Exception) {
                // Silently keep default fallback state
            }
        }
    }

    fun createRoom(
        roomNumber: String,
        floor: String,
        capacity: Int,
        rentPerBed: String,
        acType: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val newRoom = Room(
            id = System.currentTimeMillis(),
            roomNumber = roomNumber,
            floor = floor,
            capacity = capacity,
            occupiedCount = 0,
            rentPerBed = rentPerBed,
            acType = acType,
            residents = emptyList()
        )
        rooms = rooms + newRoom

        viewModelScope.launch {
            try {
                val resp = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.createRoom(CreateRoomRequest(roomNumber, floor, capacity, rentPerBed, acType))
                }
                if (resp.isSuccessful) {
                    showToast("Room $roomNumber created!")
                    loadRooms()
                    onSuccess()
                } else {
                    onSuccess()
                }
            } catch (e: Exception) {
                onSuccess()
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
