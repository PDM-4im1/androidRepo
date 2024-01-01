package tn.esprit.pdm_draft.model

import com.google.gson.annotations.SerializedName


data class Colis (
    @SerializedName("id") val id: String,
    @SerializedName("height") val height: Int,
    @SerializedName("width")val width: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("description") val description: String,
    @SerializedName("adresse") val adresse: String,
    @SerializedName("receiverName") val receiverName: String,
    @SerializedName("receiverPhone") val receiverPhone: String,
    @SerializedName("destination") val destination: String,
    @SerializedName("etat") val etat: String,



    )
