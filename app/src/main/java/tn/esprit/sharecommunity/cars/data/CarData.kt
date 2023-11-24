package tn.esprit.sharecommunity.cars.data


import com.google.gson.annotations.SerializedName

data class CarData (
    @SerializedName("id_moyen_transport")val id_moyen_transpor : Int,
    @SerializedName("marque")val marque: String,
    @SerializedName("type")val type: String,
    @SerializedName("matricule")val matricule: String,
    @SerializedName("image")val image : String,
    @SerializedName("trajet")val trajet : String,
    @SerializedName("idConducteur")val idConducteur : Int,
)