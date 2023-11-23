package tn.esprit.sharecommute03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tn.esprit.sharecommute03.databinding.ActivitySigninBinding
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.esprit.sharecommute03.api.ApiService
import tn.esprit.sharecommute03.api.UserRequest
import tn.esprit.sharecommute03.utils.MyStatics

class Signin : AppCompatActivity() {
    //retrofit initializing
    private val BASE_URL = "http://10.0.2.2:9090/" // node.js server URL
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val apiService = retrofit.create(ApiService::class.java)
    private lateinit var binding:ActivitySigninBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val contextView = binding.root
        //email
        binding.editTextEmail.addTextChangedListener(object : TextWatcher {
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
        binding.btnsignuplink.setOnClickListener {
            startActivity(Intent(this,Signup::class.java))
        }


        binding.btnForgotPassword.setOnClickListener{
            startActivity(Intent(this, ForgetPassword::class.java))
        }

        binding.buttonSignIn.setOnClickListener {
            MyStatics.hideKeyboard(this, binding.buttonSignIn)

            // Validate input fields
            if (validateEmail() && validatePassword()) {
                val signinData = UserRequest(
                    email = binding.editTextEmail.text.toString(),
                    password = binding.password.text.toString(),
                    phoneNumber = "",
                    role = "",
                    name = "",
                    firstName = "",
                    age = 0
                )

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = apiService.signin(signinData) // Make the API call and wait for the response

                        if (response.isSuccessful) {
                            // Handle successful sign-in
                            // TODO: Process user response as needed
                            Snackbar.make(
                                contextView,
                                "Signed in successfully",
                                Snackbar.LENGTH_SHORT
                            ).show()

                            // Start the next activity
                            //startActivity(Intent(this@Signin, HomeActivity::class.java))
                        } else {
                            // Handle unsuccessful sign-in (e.g., invalid input, server error)
                            val errorMessage = response.message() ?: "Unknown error"
                            Snackbar.make(
                                contextView,
                                "Failed to sign in. ${errorMessage}. Please try again.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        // Handle any exceptions that occur during the API call
                        Snackbar.make(
                            contextView,
                            "An error occurred while signing in. Please try again.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Snackbar.make(
                    contextView,
                    getString(R.string.msg_error_inputs),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }


        binding.btnReturn.setOnClickListener{
            finishAffinity()
        }
    }
    private fun validateEmail(): Boolean {
        val emailEditText = binding.editTextEmail // Assuming this is the ID of your EditText widget

        emailEditText.error = null // Remove any existing error message

        if (binding.editTextEmail.text.toString().isEmpty()) {
            emailEditText.error = getString(R.string.msg_must_not_be_empty)
            emailEditText.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(binding.editTextEmail.text.toString()).matches()) {
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
}