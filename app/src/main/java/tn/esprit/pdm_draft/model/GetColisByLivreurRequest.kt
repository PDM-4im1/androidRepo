package tn.esprit.pdm_draft.model

import com.google.gson.annotations.SerializedName

data class GetColisByLivreurRequest(
    @SerializedName("idLivreur")
    val idLivreur: Int
)