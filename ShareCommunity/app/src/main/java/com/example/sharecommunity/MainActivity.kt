package com.example.sharecommunity


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharecommunity.databinding.ActivityMainBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback
import com.jakewharton.threetenabp.AndroidThreeTen

class MainActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.startcoviturage)

        val startDrivingButton: Button = findViewById(R.id.startdrivingButton)
        startDrivingButton.setOnClickListener {
            val intent = Intent(this, MapsActivityView::class.java)
            startActivity(intent)
        }

      /*  setContentView(R.layout.activity_driver_list)


        val driverData = listOf(
            DriverData("John", "Doe", "123456789", "Car Model: ABC123"),
            DriverData("Jane", "Smith", "987654321", "Car Model: XYZ789"),
            DriverData("abouhmid","bezzine","55200800","BM X"),
            )
        val cars = listOf(
            CarData("Car 1", "ABC123", "Car Info 1"),
            CarData("Car 2", "XYZ789", "Car Info 2"),
            CarData("Car 3", "LMN456", "Car Info 3")
        )*/


  /*val recyclerView: RecyclerView = findViewById(R.id.recyclerViewDrivers)
  val adapter = DriverAdapter(driverData)
  recyclerView.layoutManager = LinearLayoutManager(this)
  recyclerView.adapter = adapter*/

    /*val recyclerView: RecyclerView = findViewById(R.id.recyclerViewCars)
val adapter = CarsAdapter(cars)
recyclerView.layoutManager = LinearLayoutManager(this)
recyclerView.adapter = adapter*/

}}
