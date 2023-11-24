package tn.esprit.pdm_draft

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tn.esprit.pdm_draft.databinding.ActivityPackageDetailsBinding
import tn.esprit.pdm_draft.databinding.ActivityReceiverDetailBinding
import tn.esprit.pdm_draft.model.Colis


class ReceiverDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityReceiverDetailBinding
    private val colisApi: ColisApi by lazy {
        RetrofitHelper.getInstance().create(ColisApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver_detail)
        binding = ActivityReceiverDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)


        

        binding.button.setOnClickListener {
            val colisData = intent.getStringExtra("description")?.let {
            Colis(
                description = it,
                width = intent.getIntExtra("width", 0),
                height = intent.getIntExtra("height", 0),
                weight = intent.getIntExtra("weight", 0),
                destination = binding.tiDestination.text.toString() ,
                adresse = binding.tiAdress.text.toString(),
                receiverName = binding.tiReceiverName.text.toString(),
                receiverPhone = binding.tiReceiverPhone.text.toString()


            )
        }
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val result = colisData?.let { it1 -> colisApi.createColis(it1) }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        // Handle exceptions
                        Log.e(this@ReceiverDetailActivity.toString(), "Error: ${e.message}",)
                    }

                }
            }

            startActivity(Intent(this, Map::class.java))
        }
    }
}
