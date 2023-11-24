package com.example.sharecommunity


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.startcoviturage)

        val startDrivingButton: Button = findViewById(R.id.startdrivingButton)
        startDrivingButton.setOnClickListener {
            val intent = Intent(this, MapsClientActivityView::class.java)
            startActivity(intent)
        }




    /*val recyclerView: RecyclerView = findViewById(R.id.recyclerViewCars)
val adapter = CarsAdapter(cars)
recyclerView.layoutManager = LinearLayoutManager(this)
recyclerView.adapter = adapter*/

}}
