package tn.esprit.pdm_draft.model

import com.google.gson.annotations.SerializedName

data class ChangeEtatColisRequest(
    @SerializedName("id")
    val id: String,
    @SerializedName("etat")
    val etat: String
)