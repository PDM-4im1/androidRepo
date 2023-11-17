package tn.esprit.sharecommute03

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tn.esprit.sharecommute03.databinding.ActivityResetPasswordBinding
import tn.esprit.sharecommute03.databinding.ActivitySigninBinding

class resetPassword : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener{
            startActivity(Intent(this,Signin::class.java))
        }
        binding.btnReturn.setOnClickListener{
            startActivity(Intent(this,otpValidation::class.java))
        }
    }
}