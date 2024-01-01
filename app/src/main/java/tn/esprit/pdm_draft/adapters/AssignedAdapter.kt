package tn.esprit.pdm_draft.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.pdm_draft.R
import tn.esprit.pdm_draft.model.Colis


class AssignedAdapter(private val assignedList: List<Colis>,private val changeEtatListener: ChangeEtatListener) :
    RecyclerView.Adapter<AssignedAdapter.AssignedHolder>() {
    interface ChangeEtatListener {
        fun onChangeEtatClicked(colis: Colis, selectedEtat: String)
    }
    inner class AssignedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textClientPosition: TextView = itemView.findViewById(R.id.textClientPosition)
        private val textContactPhone: TextView = itemView.findViewById(R.id.textContactPhone)
        private val textDestination: TextView = itemView.findViewById(R.id.textDestination)
        private val buttonChangeEtat: Button = itemView.findViewById(R.id.buttonChangeEtat)
        private val spinnerEtat: Spinner = itemView.findViewById(R.id.spinnerEtat)

        fun bind(colis: Colis) {
            textClientPosition.text = colis.adresse
            textContactPhone.text = colis.receiverPhone
            textDestination.text = colis.destination
            val etatOptions = arrayOf("non livree", "en route", "livree")
            val adapter = ArrayAdapter(itemView.context, android.R.layout.simple_spinner_item, etatOptions)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerEtat.adapter = adapter
            spinnerEtat.setSelection(etatOptions.indexOf(colis.etat))

            buttonChangeEtat.setOnClickListener {
                val selectedEtat = spinnerEtat.selectedItem.toString()
                Log.e("ahmed",selectedEtat)
                changeEtatListener.onChangeEtatClicked(colis, selectedEtat)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignedHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_assigned, parent, false)
        val etatOptions = arrayOf("non livree", "en route", "livree")
        val spinnerEtat = itemView.findViewById<Spinner>(R.id.spinnerEtat)
        val adapter = ArrayAdapter(itemView.context, android.R.layout.simple_spinner_item, etatOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEtat.adapter = adapter
       // spinnerEtat.setSelection(etatOptions.indexOf(selectedEtat))
        return AssignedHolder(itemView)
    }

    override fun onBindViewHolder(holder: AssignedHolder, position: Int) {
        val colis = assignedList[position]
        holder.bind(colis)
    }

    override fun getItemCount(): Int {
        return assignedList.size
    }
}