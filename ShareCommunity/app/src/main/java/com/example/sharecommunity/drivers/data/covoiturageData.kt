package com.example.sharecommunity.drivers.data

import com.google.gson.annotations.SerializedName

data class covoiturageData(
    @SerializedName("id_covoiturage")val id_covoiturage: Int,
    @SerializedName("id_cond")val id_cond : Int,
    @SerializedName("id_user")val id_user : Int,
    @SerializedName("pointDepart")val pointDepart: String,
    @SerializedName("pointArrivee")val pointArrivee: String,
    @SerializedName("Date")val Date: String,
    @SerializedName("Tarif")val Tarif: Int,

    )
