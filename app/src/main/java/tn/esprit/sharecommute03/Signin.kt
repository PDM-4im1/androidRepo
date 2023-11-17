package tn.esprit.sharecommute03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tn.esprit.sharecommute03.databinding.ActivitySigninBinding
import android.content.Intent

class Signin : AppCompatActivity() {
    private lateinit var binding:ActivitySigninBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnForgotPassword.setOnClickListener{
            startActivity(Intent(this, ForgetPassword::class.java))
        }
    }
}