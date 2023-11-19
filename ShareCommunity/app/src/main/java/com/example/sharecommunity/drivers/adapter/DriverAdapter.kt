package com.example.sharecommunity.drivers.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sharecommunity.drivers.data.DriverData
import com.example.sharecommunity.R

class DriverAdapter(private val drivers: List<DriverData>) : RecyclerView.Adapter<DriverAdapter.DriverViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.driverslist, parent, false)
        return DriverViewHolder(view)

    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        val driver = drivers[position]
        holder.bind(driver)
    }

    override fun getItemCount(): Int {
        return drivers.size
    }

    class DriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(driver: DriverData) {
            itemView.findViewById<TextView>(R.id.textDriverName).text = "${driver.nom} ${driver.prenom}"
            itemView.findViewById<TextView>(R.id.textContactPhone).text = driver.contactPhone
            itemView.findViewById<TextView>(R.id.textVehicleInfo).text = driver.vehicleInfo
        }
    }
}


