package tn.esprit.sharecommute03

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tn.esprit.sharecommute03.databinding.ActivityForgePasswordBinding
import tn.esprit.sharecommute03.databinding.ActivityOtpValidationBinding

class otpValidation : AppCompatActivity() {
    private lateinit var binding: ActivityOtpValidationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpValidationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVerify.setOnClickListener{
            startActivity(Intent(this,resetPassword::class.java))
        }

        binding.btnReturn.setOnClickListener{
            startActivity(Intent(this,ForgetPassword::class.java))
        }

    }
}