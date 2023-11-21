package com.example.sharecommunity.drivers.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sharecommunity.R
import com.example.sharecommunity.cars.data.CarData
import com.example.sharecommunity.drivers.data.DriverData
import com.example.sharecommunity.drivers.data.UserData
import com.example.sharecommunity.drivers.data.UserRole
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class DriverAdapter : RecyclerView.Adapter<DriverAdapter.DriverViewHolder>() {

    private val drivers: MutableList<DriverData> = mutableListOf()
    private val userData: MutableList<UserData> = mutableListOf()
    private val carData: MutableList<CarData> = mutableListOf()

    fun setData(drivers: List<DriverData>, userData: List<UserData>, carData: List<CarData>) {

        this.drivers.clear()
        this.drivers.addAll(drivers)

        this.userData.clear()
        this.userData.addAll(userData)

        this.carData.clear()
        this.carData.addAll(carData)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.driverslist, parent, false)
        return DriverViewHolder(view)

    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        val driver = drivers[position]
        val userDataList = userData.filter { it.id_user == driver.id_user }
        val carDataList = carData.filter { it.id_moyen_transpor == driver.id_moyen_transpor }

        if (userDataList.isNotEmpty() && carDataList.isNotEmpty()) {
            val userData = userDataList[0]
            val carData = carDataList[0]
            holder.bind(driver, userData, carData)
        }
    }

    override fun getItemCount(): Int {
        return drivers.size
    }

    class DriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(driver: DriverData, userData: UserData, carData: CarData) {
            Log.e("DriverAdapter", "first_name: ${userData.first_name}, name: ${userData.name}, Phone_number: ${userData.Phone_number}")
            itemView.findViewById<TextView>(R.id.textDriverName).text = "${userData.first_name} ${userData.name}"
            itemView.findViewById<TextView>(R.id.textContactPhone).text = userData.Phone_number.toString()
            itemView.findViewById<TextView>(R.id.textVehicleInfo).text = carData.marque
        }
    }
}
