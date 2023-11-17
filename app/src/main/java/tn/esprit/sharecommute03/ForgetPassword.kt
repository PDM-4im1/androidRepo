package tn.esprit.sharecommute03

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tn.esprit.sharecommute03.databinding.ActivityForgePasswordBinding
import tn.esprit.sharecommute03.databinding.ActivityMainBinding

class ForgetPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener{
            startActivity(Intent(this,otpValidation::class.java))
        }
        binding.btnSendSMS.setOnClickListener{
            startActivity(Intent(this,otpValidation::class.java))
        }
        binding.btnReturn.setOnClickListener{
            startActivity(Intent(this,Signin::class.java))
        }
    }
}