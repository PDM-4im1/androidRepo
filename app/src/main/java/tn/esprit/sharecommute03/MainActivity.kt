package tn.esprit.sharecommute03

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tn.esprit.sharecommute03.databinding.ActivityMainBinding
import tn.esprit.sharecommute03.databinding.ActivitySignupBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp0.setOnClickListener{
            startActivity(Intent(this, Signup::class.java))
        }

        binding.SignInLink.setOnClickListener {
            startActivity(Intent(this,Signin::class.java))
        }

    }
}