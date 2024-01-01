package tn.esprit.pdm_draft.adapters

import android.service.autofill.UserData
import android.text.method.TextKeyListener.clear
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import tn.esprit.pdm_draft.ColisApi
import tn.esprit.pdm_draft.R
import tn.esprit.pdm_draft.RetrofitHelper
import tn.esprit.pdm_draft.databinding.ActivityDeliveryBinding
import tn.esprit.pdm_draft.model.AssignLivreurRequest
import tn.esprit.pdm_draft.model.Colis
import retrofit2.Callback
import retrofit2.Response

class DemandAdapter :RecyclerView.Adapter<DemandAdapter.DemandHolder>() {

    private val demand: MutableList<Colis> = mutableListOf()
    private var onAssignClickListener: OnAssignClickListener? = null

    fun setOnAssignClickListener(listener: OnAssignClickListener) {
        onAssignClickListener = listener
    }

    fun setData(demand: List<Colis>) {


        this.demand.addAll(demand)

        notifyDataSetChanged()
    }

    interface OnAssignClickListener {
        fun onAssignClick(colisId: String)
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
        private val colisApi: ColisApi by lazy {
            RetrofitHelper.getInstance().create(ColisApi::class.java)
        }
        init {
            itemView.findViewById<LinearLayout>(R.id.id_assign).setOnClickListener {
                // Get the colis ID and livreur ID here
                val colisId = demand[adapterPosition].id

                Log.e("hama",colisId);
                val livreurId = "112" // Replace with the actual livreur ID

                val request = AssignLivreurRequest(colisId, livreurId)
                assignLivreur(request)
               // onAssignClickListener?.onAssignClick(colisId)
            }
        }

        private fun assignLivreur(request: AssignLivreurRequest) {
            colisApi.assignerLivreur(request).enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        demand.removeAt(adapterPosition)
                        notifyDataSetChanged()
                      //  onAssignClickListener?.onAssignClick(request.id)

                        // Handle successful response
                    } else {
                        // Handle unsuccessful response
                    }
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    // Handle failure
                }
            })
        }
        fun bind(demand:Colis){
            itemView.findViewById<TextView>(R.id.textClientPosition).text = demand.adresse
            itemView.findViewById<TextView>(R.id.textContactPhone).text = demand.receiverPhone
            itemView.findViewById<TextView>(R.id.textDestination).text = demand.destination
            itemView.findViewById<TextView>(R.id.textDestination).text = demand.destination

        }
    }

}