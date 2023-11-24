package tn.esprit.sharecommunity.drivers.data

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("id") val id_user: Int,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("Phone_number") val Phone_number: Int,
    @SerializedName("role") val role: String,
    @SerializedName("name") val name: String,
    @SerializedName("first_name") val first_name : String,
    @SerializedName("age") val age: Int,
)
