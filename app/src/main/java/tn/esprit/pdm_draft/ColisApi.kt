package tn.esprit.pdm_draft

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import tn.esprit.pdm_draft.model.Colis

interface ColisApi {
    @GET("/colis")
     fun findAll() : Call<List<Colis>>
    @POST("/colis")
    suspend fun createColis(@Body colis: Colis): Response<Colis>
}