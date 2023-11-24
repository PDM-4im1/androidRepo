package com.example.sharecommunity.drivers.services
import com.example.sharecommunity.cars.data.CarData
import com.example.sharecommunity.drivers.data.DriverData
import com.example.sharecommunity.drivers.data.UserData
import com.example.sharecommunity.drivers.data.covoiturageData
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface DriverServices {
    @PUT("/covoiturage/saveCovoiturage")
    fun saveCovoiturage(@Body requestBody: RequestBody): Call<covoiturageData>
    @PUT("/moyenDeTransport/saveMoyenDeTransport")
    fun saveCars(@Body requestBody: RequestBody): Call<List<CarData>>
    @GET("/moyenDeTransport/findAllMoyenDeTransport")
    fun getCars(): Call<List<CarData>>
    @GET("/moyenDeTransport/findAllDrivers")
    fun getDrivers(): Call<List<DriverData>>
    @GET("/moyenDeTransport/findAllUsers")
    fun getUsers(): Call<List<UserData>>


}