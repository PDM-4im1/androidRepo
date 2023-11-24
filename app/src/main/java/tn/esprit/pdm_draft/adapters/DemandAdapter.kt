package tn.esprit.pdm_draft.adapters

import android.service.autofill.UserData
import android.text.method.TextKeyListener.clear
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.pdm_draft.R
import tn.esprit.pdm_draft.databinding.ActivityDeliveryBinding
import tn.esprit.pdm_draft.model.Colis

class DemandAdapter :RecyclerView.Adapter<DemandAdapter.DemandHolder>() {

    private val demand: MutableList<Colis> = mutableListOf()

    fun setData(demand: List<Colis>) {


        this.demand.addAll(demand)

        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemandHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_delivery, parent, false)
        return DemandHolder(view)
    }

    override fun onBindViewHolder(holder: DemandHolder, position: Int) {

            val demands =demand[position]
            holder.bind(demands)

    }

    override fun getItemCount(): Int{
        return demand.size
    }


    inner class DemandHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(demand:Colis){
            itemView.findViewById<TextView>(R.id.textClientPosition).text = demand.adresse
            itemView.findViewById<TextView>(R.id.textContactPhone).text = demand.receiverPhone
            itemView.findViewById<TextView>(R.id.textDestination).text = demand.destination
        }
    }

}