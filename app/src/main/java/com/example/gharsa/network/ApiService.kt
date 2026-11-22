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
}
