package com.example.sharecommunity.drivers.data

import com.google.gson.annotations.SerializedName

data class DriverData(
    @SerializedName("id_cond")val id_cond: Int,
    @SerializedName("id_moyen_transpor")val id_moyen_transpor : Int,
    @SerializedName("id_user")val id_user : Int,
    @SerializedName("pointDepart")val pointDepart: String,
    @SerializedName("pointArrivee")val pointArrivee: String,
    @SerializedName("localisation")val localisation: String,
)
