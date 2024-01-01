package tn.esprit.pdm_draft

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.pdm_draft.adapters.AssignedAdapter
import tn.esprit.pdm_draft.databinding.ActivityAssignedBinding
import tn.esprit.pdm_draft.databinding.ActivityDemandListBinding
import tn.esprit.pdm_draft.model.ChangeEtatColisRequest

import tn.esprit.pdm_draft.model.Colis
import tn.esprit.pdm_draft.model.GetColisByLivreurRequest


class AssignedList : AppCompatActivity(), AssignedAdapter.ChangeEtatListener {

    private lateinit var assignedAdapter: AssignedAdapter
    private lateinit var spinnerEtat: Spinner
    private lateinit var binding: ActivityAssignedBinding
    private lateinit var recyclerView: RecyclerView
    private val colisApi: ColisApi by lazy {
        RetrofitHelper.getInstance().create(ColisApi::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assigned_list)

        binding = ActivityAssignedBinding.inflate(layoutInflater)
        spinnerEtat = binding.spinnerEtat
        val buttonChangeEtat = binding.buttonChangeEtat
        val etatOptions = arrayOf("non livree", "en route", "livree")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, etatOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEtat.adapter = adapter

        buttonChangeEtat.setOnClickListener {
            val selectedEtat = spinnerEtat.selectedItem.toString()
            // Handle the button click event
            // Add your code here
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        assignedAdapter = AssignedAdapter(emptyList(), this)
        recyclerView.adapter = assignedAdapter



        fetchDataAndUpdateAdapter()
    }

    private fun fetchDataAndUpdateAdapter() {

        val idLivreur = 112 // Replace with the actual id of the livreur
        val request = GetColisByLivreurRequest(idLivreur)
        val call = colisApi.getColisByLivreur(request)
        call.enqueue(object : Callback<List<Colis>> {
            override fun onResponse(call: Call<List<Colis>>, response: Response<List<Colis>>) {
                if (response.isSuccessful) {
                    val assignedColisList = response.body() ?: emptyList()
                    assignedAdapter = AssignedAdapter(assignedColisList,this@AssignedList)
                    recyclerView.adapter = assignedAdapter
                    assignedAdapter.notifyDataSetChanged()
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<List<Colis>>, t: Throwable) {
                // Handle failure
            }
        })
    }
    override fun onChangeEtatClicked(colis: Colis, selectedEtat: String) {
        //val selectedEtat = binding.spinnerEtat.selectedItem.toString()

        Log.e("ahmed",colis.id)

        // Make the API call to changeEtatColis
        val request = ChangeEtatColisRequest(colis.id, selectedEtat)
        val call = colisApi.changeEtatColis(request)





        call.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    Toast.makeText(applicationContext, "Etat changed successfully", Toast.LENGTH_SHORT).show()
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(applicationContext, "Failed to change etat", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                // Handle failure
                Toast.makeText(applicationContext, "Failed to change etat: " + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}