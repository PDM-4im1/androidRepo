import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.sharecommunity.R
import tn.esprit.sharecommunity.drivers.data.DriverData

class EmDriverAdapter(private val driverList: List<DriverData>) :
    RecyclerView.Adapter<EmDriverAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.emgcovoiturage, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val driver = driverList[position]
        holder.bind(driver)
    }

    override fun getItemCount(): Int = driverList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameLabel: TextView = itemView.findViewById(R.id.nameLabel)
        private val phoneLabel: TextView = itemView.findViewById(R.id.phoneLabel)
        private val carLabel: TextView = itemView.findViewById(R.id.carLabel)
        private val priceLabel: TextView = itemView.findViewById(R.id.priceLabel)

        fun bind(driver: DriverData) {
            nameLabel.text = driver.name
            phoneLabel.text = driver.phone
            carLabel.text = driver.car
            priceLabel.text = driver.price
        }
    }
}
