package tn.esprit.sharecommute03

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import tn.esprit.sharecommute03.databinding.ActivitySignupBinding
import tn.esprit.sharecommute03.utils.MyStatics




class Signup : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val contextView = binding.root
        //email
        binding.Email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail()
            }

            override fun afterTextChanged(s: Editable?) {
                return
            }
        })
        //password
        binding.password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword()
            }

            override fun afterTextChanged(s: Editable?) {
                return
            }
        })
        //confirmPassword
        binding.confirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateCpassword()
            }

            override fun afterTextChanged(s: Editable?) {
                return
            }
        })
        //phone number
        binding.phoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePhoneNumber()
            }

            override fun afterTextChanged(s: Editable?) {
                return
            }
        })
        //button signup click
        binding.btnSignUp.setOnClickListener {
            MyStatics.hideKeyboard(this, binding.btnSignUp)
            if (validateEmail() && validatePassword() && validateCpassword() && validatePhoneNumber()) {
                startActivity(Intent(this, Signin::class.java))
            } else {
                Snackbar.make(
                    contextView,
                    getString(R.string.msg_error_inputs),
                    Snackbar.LENGTH_SHORT
                ).show()
            }

        }

        binding.SigninLink.setOnClickListener{
            MyStatics.hideKeyboard(this, binding.btnSignUp)
            startActivity(Intent(this, Main_signin::class.java))
        }

    }

    private fun validateEmail(): Boolean {
        val emailEditText = binding.Email // Assuming this is the ID of your EditText widget

        emailEditText.error = null // Remove any existing error message

        if (emailEditText.text.toString().isEmpty()) {
            emailEditText.error = getString(R.string.msg_must_not_be_empty)
            emailEditText.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()) {
            emailEditText.error = getString(R.string.msg_check_your_email)
            emailEditText.requestFocus()
            return false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        val passwordEditText = binding.password // Assuming this is the ID of your EditText widget
        passwordEditText.error = null // Remove any existing error message

        if (binding.password.text.toString().isEmpty()) {
            passwordEditText.error = getString(R.string.msg_must_not_be_empty)
            binding.password.requestFocus()
            return false
        } else {
            passwordEditText.error = null
        }

        if (binding.password.text.toString().length < 6) {
            passwordEditText.error = getString(R.string.msg_check_your_characters)
            binding.password.requestFocus()
        }
        return true
    }

    private fun validateCpassword(): Boolean {
        val passwordEditText = binding.password // Assuming this is the ID of your EditText widget
        passwordEditText.error = null // Remove any existing error message
        val CpasswordEditText = binding.confirmPassword
        CpasswordEditText.error = null

        if (binding.password.text.toString().isEmpty()) {
            CpasswordEditText.error = getString(R.string.msg_must_not_be_empty)
            binding.password.requestFocus()
            return false
        } else {
            CpasswordEditText.error = null
        }
        if (binding.password.text.toString().length < 6) {
            CpasswordEditText.error = getString(R.string.msg_check_your_characters)
            binding.password.requestFocus()
        }
        if (!binding.confirmPassword.text.toString().equals(binding.password.text.toString())) {
            CpasswordEditText.error = getString(R.string.msg_check_your_confirm_Password)
            CpasswordEditText.error = getString(R.string.msg_check_your_confirm_Password)
            binding.confirmPassword.requestFocus()
            return false
        } else {
            CpasswordEditText.error = null
            passwordEditText.error = null
        }

        return true
    }

    private fun validatePhoneNumber(): Boolean {
        val phoneNumbertext = binding.phoneNumber
        phoneNumbertext.error = null

        if (binding.phoneNumber.text.toString().isEmpty()) {
            phoneNumbertext.error = getString(R.string.msg_must_not_be_empty)
            binding.password.requestFocus()
            return false
        } else {
            phoneNumbertext.error = null
        }
        if (!binding.phoneNumber.text.toString().matches("^\\d{8}\$".toRegex())) { // Check if the phone number consists of only 8 digits
            phoneNumbertext.error = getString(R.string.msg_invalid_phone_number)
            binding.phoneNumber.requestFocus()
            return false
        }
        return true
    }
}