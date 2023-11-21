package com.example.sharecommunity.drivers.services
import com.example.sharecommunity.cars.data.CarData
import com.example.sharecommunity.drivers.data.DriverData
import com.example.sharecommunity.drivers.data.UserData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT

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