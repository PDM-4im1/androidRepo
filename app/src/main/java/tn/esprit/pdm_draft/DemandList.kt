package tn.esprit.pdm_draft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tn.esprit.pdm_draft.adapters.DemandAdapter
import tn.esprit.pdm_draft.databinding.ActivityDemandListBinding
import tn.esprit.pdm_draft.model.Colis
import kotlin.math.log

class DemandList : AppCompatActivity(), DemandAdapter.OnAssignClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var demandAdapter: DemandAdapter
    private lateinit var binding: ActivityDemandListBinding
    private val colisApi: ColisApi by lazy {
        RetrofitHelper.getInstance().create(ColisApi::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDemandListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.recyclerViewDemand)
        demandAdapter = DemandAdapter()
        demandAdapter.setOnAssignClickListener(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = demandAdapter

        fetchDataAndUpdateAdapter()




    }
    override fun onAssignClick(colisId: String) {
        // Refresh the view or perform any other actions
        fetchDataAndUpdateAdapter()
    }
    private fun fetchDataAndUpdateAdapter() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = colisApi.findAll().execute()
                withContext(Dispatchers.Main) {
                    if(result.isSuccessful){

                        val colis = result.body() ?: emptyList()
                        Log.e(colis.toString()," ")
                        demandAdapter.setData(colis)
                        updateRecyclerView()


                    }
                }
            }
            catch (e: Exception){
                withContext(Dispatchers.Main) {
                    // Handle exceptions
                    Log.e(this@DemandList.toString(), "Error: ${e.message}", )
                }

            }

        }
    }


    private fun updateRecyclerView() {
        setContentView(R.layout.activity_demand_list)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewDemand)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = demandAdapter
    }
}


