package tn.esprit.sharecommunity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.esprit.sharecommunity.cars.data.CarData
import tn.esprit.sharecommunity.drivers.adapter.EmDriverAdapter
import tn.esprit.sharecommunity.drivers.data.DriverData
import tn.esprit.sharecommunity.drivers.data.UserData
import tn.esprit.sharecommunity.drivers.services.EmDriverService

class EmgCovoiturageActivity : AppCompatActivity() {

    private val driverServices: EmDriverService by lazy {
        createRetrofit().create(EmDriverService::class.java)
    }
    private val driverAdapter = EmDriverAdapter()
    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:9090/") // Replace with your actual backend URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_emgcovoiturage)
    }
    fun updateRecyclerView( driverAdapter : EmDriverAdapter) {
        setContentView(R.layout.fragment_emgcovoiturage)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewEMDrivers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = driverAdapter
    }
    fun fetchDriverData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val driversResponse =  listOf(
                DriverData(id_cond = 5,id_moyen_transpor = 1, id_user = 1, pointDepart="A",pointArrivee="B", localisation = "YourLocation"),
                DriverData(id_cond = 6,id_moyen_transpor = 1, id_user = 2, pointDepart="A",pointArrivee="B", localisation = "YourLocation"),
                DriverData(id_cond = 7,id_moyen_transpor = 1, id_user = 3, pointDepart="A",pointArrivee="B", localisation = "YourLocation"),

                )
                val carsResponse = listOf(
                    CarData(id_moyen_transpor = 1, marque = "Toyota", type = "vehicule",matricule = "123TN7895",image = "aaaa",trajet = "15746",idConducteur = 5
                    ),
                )
                val usersResponse = listOf(
                    UserData( id_user = 1, email="aaa.vvv@ggg.cc" ,password = "aaaaaa",Phone_number = 22555999 ,role ="Driver",name="mizo", first_name = "John",  age = 22),
                    UserData( id_user = 2, email="zzzz.gggg@vvv.cc" ,password = "sssss",Phone_number = 22478596 ,role ="Driver", first_name = "Dai", name = "Doe", age = 25),
                    UserData( id_user = 3, email="hhh.fff@nn.cc" ,password = "pppp",Phone_number = 20147852 ,role ="Driver",first_name = "dido", name = "mansour", age = 24),

                    )


                    if (driversResponse.isNotEmpty() && carsResponse.isNotEmpty() && usersResponse.isNotEmpty()) {
                        val drivers = driversResponse ?: emptyList()
                        val cars = carsResponse ?: emptyList()
                        val users = usersResponse ?: emptyList()

                        driverAdapter.setData(drivers, users, cars,"tunis")

                        updateRecyclerView(driverAdapter)

                        //showDriversBtn.isEnabled = true

                }
            } catch (e: Exception) {

                Log.d("Error : ","${e.message}")

            }

        }
    }
}