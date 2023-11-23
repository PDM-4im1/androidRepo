package tn.esprit.sharecommute03.api

import com.google.gson.annotations.SerializedName

// Models.kt

data class UserRequest(
    @SerializedName("email")val email: String,
    @SerializedName("password")val password: String,
    @SerializedName("Phone_number")val phoneNumber: String,
    @SerializedName("role")val role: String,
    @SerializedName("name")val name: String,
    @SerializedName("first_name")val firstName: String,
    @SerializedName("age")val age: Int,
)

data class UserResponse(
    val id: Int,
    val email: String,
    val phoneNumber: String,
    val role: String,
)


