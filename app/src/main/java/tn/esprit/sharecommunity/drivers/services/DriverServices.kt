package tn.esprit.sharecommunity.drivers.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import tn.esprit.sharecommunity.cars.data.CarData
import tn.esprit.sharecommunity.drivers.data.DriverData
import tn.esprit.sharecommunity.drivers.data.UserData

interface DriverServices {
    @PUT("/moyenDeTransport/saveMoyenDeTransport")
    fun saveCars(): Call<List<CarData>>
    @GET("/moyenDeTransport/findAllMoyenDeTransport")
    fun getCars(): Call<List<CarData>>
    @GET("/moyenDeTransport/findAllDrivers")
    fun getDrivers(): Call<List<DriverData>>

    @GET("/moyenDeTransport/findAllUsers")
    fun getUsers(): Call<List<UserData>>


}