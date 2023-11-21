package com.example.sharecommunity.cars.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sharecommunity.cars.data.CarData
import com.example.sharecommunity.R

class CarsAdapter(private val cars: List<CarData>) : RecyclerView.Adapter<CarsAdapter.CarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carslist, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = cars[position]
        holder.bind(car)
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(car: CarData) {
            itemView.findViewById<TextView>(R.id.textCarName).text = car.marque
            itemView.findViewById<TextView>(R.id.textCarMatricule).text = car.matricule
            itemView.findViewById<TextView>(R.id.textCarInfo).text = car.type
        }
    }
}
