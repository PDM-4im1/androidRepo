package tn.esprit.pdm_draft

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import tn.esprit.pdm_draft.model.AssignLivreurRequest
import tn.esprit.pdm_draft.model.ChangeEtatColisRequest
import tn.esprit.pdm_draft.model.Colis
import tn.esprit.pdm_draft.model.GetColisByLivreurRequest

interface ColisApi {
    @GET("/colis/getunassigned")
     fun findAll() : Call<List<Colis>>
    @POST("/colis")
    suspend fun createColis(@Body colis: Colis): Response<Colis>
    @POST("/colis/assign")
    fun assignerLivreur(@Body requestBody: AssignLivreurRequest): Call<Any>

    @POST("/colis/changeEtatColis")
    fun changeEtatColis(@Body requestBody: ChangeEtatColisRequest): Call<Any>

    @POST("/colis/getColisByLivreur")
    fun getColisByLivreur(@Body requestBody: GetColisByLivreurRequest): Call<List<Colis>>

}