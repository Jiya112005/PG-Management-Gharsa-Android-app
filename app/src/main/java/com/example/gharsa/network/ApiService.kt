package com.example.gharsa.network

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class SignUpRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("role") val role: String,
    @SerializedName("roomNumber") val roomNumber: String,
    @SerializedName("mobile") val mobile: String
)

data class UpdateProfileRequest(
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("roomNumber") val roomNumber: String,
    @SerializedName("password") val password: String
)

data class UserResponse(
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("role") val role: String,
    @SerializedName("roomNumber") val roomNumber: String,
    @SerializedName("mobile") val mobile: String?
)

data class SendOtpRequest(
    @SerializedName("mobile") val mobile: String
)

data class VerifyOtpRequest(
    @SerializedName("mobile") val mobile: String,
    @SerializedName("otp") val otp: String
)

data class OtpResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("demoOtp") val demoOtp: String?
)

data class TicketDto(
    @SerializedName("id") val id: Long,
    @SerializedName("residentEmail") val residentEmail: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("status") val status: String,
    @SerializedName("date") val date: String
)

data class CreateTicketRequest(
    @SerializedName("residentEmail") val residentEmail: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("date") val date: String
)

data class RentDto(
    @SerializedName("id") val id: Long,
    @SerializedName("residentEmail") val residentEmail: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("status") val status: String,
    @SerializedName("dueDate") val dueDate: String,
    @SerializedName("month") val month: String
)

data class PayRentRequest(
    @SerializedName("rentId") val rentId: Long
)

// ================= TENANT MANAGEMENT MODELS =================

data class TenantDto(
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("mobile") val mobile: String?,
    @SerializedName("roomNumber") val roomNumber: String,
    @SerializedName("bedNumber") val bedNumber: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("emergencyContact") val emergencyContact: String?,
    @SerializedName("joiningDate") val joiningDate: String?,
    @SerializedName("status") val status: String?, // ACTIVE, INACTIVE, PENDING
    @SerializedName("monthlyRent") val monthlyRent: String?,
    @SerializedName("rentStatus") val rentStatus: String?
)

data class AddTenantRequest(
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String,
    @SerializedName("address") val address: String,
    @SerializedName("emergencyContact") val emergencyContact: String,
    @SerializedName("joiningDate") val joiningDate: String,
    @SerializedName("roomNumber") val roomNumber: String,
    @SerializedName("bedNumber") val bedNumber: String,
    @SerializedName("monthlyRent") val monthlyRent: String
)

data class EditTenantRequest(
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("address") val address: String,
    @SerializedName("emergencyContact") val emergencyContact: String,
    @SerializedName("joiningDate") val joiningDate: String,
    @SerializedName("roomNumber") val roomNumber: String,
    @SerializedName("bedNumber") val bedNumber: String,
    @SerializedName("monthlyRent") val monthlyRent: String,
    @SerializedName("status") val status: String
)

data class TenantStatusRequest(
    @SerializedName("email") val email: String,
    @SerializedName("status") val status: String
)

// ================= ROOM MANAGEMENT MODELS =================

data class RoomResidentDto(
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("roomNumber") val roomNumber: String,
    @SerializedName("bedNumber") val bedNumber: String?,
    @SerializedName("mobile") val mobile: String?
)

data class RoomDto(
    @SerializedName("id") val id: Long,
    @SerializedName("roomNumber") val roomNumber: String,
    @SerializedName("floor") val floor: String,
    @SerializedName("capacity") val capacity: Int,
    @SerializedName("occupiedCount") val occupiedCount: Int,
    @SerializedName("rentPerBed") val rentPerBed: String,
    @SerializedName("acType") val acType: String,
    @SerializedName("residents") val residents: List<RoomResidentDto>?
)

data class CreateRoomRequest(
    @SerializedName("roomNumber") val roomNumber: String,
    @SerializedName("floor") val floor: String,
    @SerializedName("capacity") val capacity: Int,
    @SerializedName("rentPerBed") val rentPerBed: String,
    @SerializedName("acType") val acType: String
)

data class AssignRoomRequest(
    @SerializedName("email") val email: String,
    @SerializedName("roomNumber") val roomNumber: String,
    @SerializedName("bedNumber") val bedNumber: String
)

interface ApiService {
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<UserResponse>

    @POST("api/signup")
    suspend fun signUp(@Body request: SignUpRequest): Response<UserResponse>

    @POST("api/profile/update")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<UserResponse>

    @POST("api/send-otp")
    suspend fun sendOtp(@Body request: SendOtpRequest): Response<OtpResponse>

    @POST("api/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<UserResponse>

    @GET("api/tickets")
    suspend fun getTickets(@Query("email") email: String): Response<List<TicketDto>>

    @POST("api/tickets")
    suspend fun createTicket(@Body request: CreateTicketRequest): Response<TicketDto>

    @GET("api/rents")
    suspend fun getRents(@Query("email") email: String): Response<List<RentDto>>

    @POST("api/rents/pay")
    suspend fun payRent(@Body request: PayRentRequest): Response<RentDto>

    @GET("api/tenants")
    suspend fun getTenants(): Response<List<TenantDto>>

    @POST("api/tenants/add")
    suspend fun addTenant(@Body request: AddTenantRequest): Response<Unit>

    @POST("api/tenants/edit")
    suspend fun editTenant(@Body request: EditTenantRequest): Response<Unit>

    @POST("api/tenants/status")
    suspend fun setTenantStatus(@Body request: TenantStatusRequest): Response<Unit>

    @GET("api/rooms")
    suspend fun getRooms(): Response<List<RoomDto>>

    @POST("api/rooms")
    suspend fun createRoom(@Body request: CreateRoomRequest): Response<RoomDto>

    @POST("api/rooms/assign")
    suspend fun assignRoom(@Body request: AssignRoomRequest): Response<Unit>
}
