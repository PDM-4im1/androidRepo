package tn.esprit.sharecommute03

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tn.esprit.sharecommute03.databinding.ActivityMainSigninBinding
import tn.esprit.sharecommute03.databinding.ActivitySigninBinding

class Main_signin : AppCompatActivity() {
    private lateinit var binding: ActivityMainSigninBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainSigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignIn.setOnClickListener{
            startActivity(Intent(this, Signin::class.java))
        }

    }
}