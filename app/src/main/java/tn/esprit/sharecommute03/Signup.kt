package tn.esprit.sharecommute03

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Callback
import tn.esprit.sharecommute03.databinding.ActivitySignupBinding
import tn.esprit.sharecommute03.utils.MyStatics
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.esprit.sharecommute03.api.ApiService
import tn.esprit.sharecommute03.api.UserRequest
import tn.esprit.sharecommute03.api.UserResponse



class Signup : AppCompatActivity() {
    //retrofit initializing
    private val BASE_URL = "http://10.0.2.2:9090/" // node.js server URL
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val apiService = retrofit.create(ApiService::class.java)


    private lateinit var binding: ActivitySignupBinding
    private lateinit var checkBoxes: List<CheckBox>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val contextView = binding.root

        val checkBoxClient = binding.checkBoxClient
        val checkBoxDeliveryMan = binding.checkBoxDeliveryMan
        val checkBoxDriver = binding.checkBoxDriver

        // Role variable to store the role value based on checked checkboxes
        var role = ""




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

        // Assuming contextView is defined and accessible in the scope

// button signup click
        binding.btnSignUp.setOnClickListener {
            MyStatics.hideKeyboard(this, binding.btnSignUp)

            // Validate input fields and checkboxes
            if (validateEmail() && validatePassword() && validatePhoneNumber() && validateCheckBoxes()) {
                // Determine the role based on which checkboxes are checked
                if (checkBoxDriver.isChecked) {
                    role = "driver"
                } else if (checkBoxClient.isChecked) {
                    role = "client"
                } else if (checkBoxDeliveryMan.isChecked) {
                    role = "delivery man"
                }
                // Prepare the data for sign-up
                val signUpData = UserRequest(
                    email = binding.Email.text.toString(),
                    password = binding.password.text.toString(),
                    phoneNumber = binding.phoneNumber.text.toString(),
                    role = role,
                    name = "",
                    firstName = "",
                    age = 0
                )

                // Perform the API call to sign up the user using CoroutineScope
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = apiService.signup(signUpData)
                        if (response.isSuccessful) {
                            // Handle successful sign-up
                            val userResponse = response.body()
                            // Process user response as needed
                            startActivity(Intent(this@Signup, Signin::class.java))
                        } else {
                            // Handle unsuccessful sign-up (e.g., invalid input, server error)
                            val errorMessage = response.message() ?: "Unknown error"
                            Snackbar.make(
                                contextView,
                                "Failed to sign up. ${errorMessage}. Please try again.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        // Handle exceptions (e.g., network errors)
                        Snackbar.make(
                            contextView,
                            "Failed to sign up. ${e.message}. Please try again.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                // Show error message for invalid input
                Snackbar.make(
                    contextView,
                    getString(R.string.msg_error_inputs),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }







        binding.btnReturn.setOnClickListener {
            finish()
        }

        binding.SigninLink.setOnClickListener {
            MyStatics.hideKeyboard(this, binding.btnSignUp)
            startActivity(Intent(this, Main_signin::class.java))
        }
        checkBoxes = listOf(
            binding.checkBoxDriver,
            binding.checkBoxClient,
            binding.checkBoxDeliveryMan
        )

        setupCheckBoxListeners()

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

    private fun isAtLeastOneCheckBoxChecked(): Boolean {
        for (checkBox in checkBoxes) {
            if (checkBox.isChecked) {
                return true // At least one checkbox is checked
            }
        }
        return false // No checkbox is checked
    }

    private fun validateCheckBoxes(): Boolean {
        if (!isAtLeastOneCheckBoxChecked()) {
            // Show an error message since no checkbox is checked
            Snackbar.make(
                binding.root,
                "Please select at least one option", // Change this message as per your requirement
                Snackbar.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }


    private fun setupCheckBoxListeners() {
        for (checkBox in checkBoxes) {
            checkBox.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    uncheckOtherCheckboxes(checkBox)
                    changeTextColor(checkBox, isChecked)
                }
            }
        }
    }

    private fun uncheckOtherCheckboxes(currentCheckBox: CheckBox) {
        for (checkBox in checkBoxes) {
            if (checkBox != currentCheckBox) {
                checkBox.isChecked = false
            }
        }
    }
    @Suppress("DEPRECATION")
    private fun changeTextColor(checkBox: CheckBox, isChecked: Boolean) {
        if (isChecked) {
            checkBox.setTextColor(resources.getColor(R.color.green))
        } else {
            checkBox.setTextColor(resources.getColor(android.R.color.black))
        }
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

private fun UserResponse.enqueue(callback: Callback<UserResponse>) {

}
