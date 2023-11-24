package tn.esprit.pdm_draft

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tn.esprit.pdm_draft.databinding.ActivityPackageDetailsBinding
import tn.esprit.pdm_draft.databinding.ActivityReceiverDetailBinding
import kotlin.collections.Map

class PackageDetailsActivity : AppCompatActivity() {
    private lateinit var bindingP : ActivityPackageDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_details)

        bindingP = ActivityPackageDetailsBinding.inflate(layoutInflater)
        setContentView(bindingP.root)
        bindingP.button.setOnClickListener {
            val intent = Intent(this, ReceiverDetailActivity::class.java).apply {
                putExtra("description", bindingP.tiDescription.text.toString())
                putExtra("width", bindingP.tiWidth.text.toString().toIntOrNull() ?: 0)
                putExtra("height", bindingP.tiHeight.text.toString().toIntOrNull() ?: 0)
                putExtra("weight", bindingP.tiWeight.text.toString().toIntOrNull() ?: 0)
            }
            startActivity(intent)

        }
    }
}