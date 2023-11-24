package tn.esprit.sharecommunity.drivers.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.sharecommunity.R
import tn.esprit.sharecommunity.cars.data.CarData
import tn.esprit.sharecommunity.drivers.data.DriverData
import tn.esprit.sharecommunity.drivers.data.UserData

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.emgcovoiturage, parent, false)
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
            //itemView.findViewById<TextView>(R.id.textDriverName).text = "${userData.first_name} ${userData.name}"
            //itemView.findViewById<TextView>(R.id.textContactPhone).text = userData.Phone_number.toString()
            //itemView.findViewById<TextView>(R.id.textVehicleInfo).text = carData.marque
        }
    }
}