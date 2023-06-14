package com.example.speaksure_capstone.ui.register

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.speaksure_capstone.databinding.ActivityRegisterBinding
import com.example.speaksure_capstone.ui.login.LoginActivity
import com.example.speaksure_capstone.network.ApiConfig
import com.example.speaksure_capstone.response.LoginRegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        binding.btnToLogin.setOnClickListener{
            val toLogin = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(toLogin)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }


    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            val name = binding.editName.text.toString()
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()
            when {
                name.isEmpty() -> {
                    binding.editName.error = "Masukkan username"
                }
                email.isEmpty() -> {
                    binding.editEmail.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.editPassword.error = "Masukkan password"
                }
                else -> {
                    showLoading(true)
                    val client = ApiConfig.getApiService().register(email, name, password)
                    client.enqueue(object : Callback<LoginRegisterResponse> {
                        override fun onResponse(call: Call<LoginRegisterResponse>, response: Response<LoginRegisterResponse>) {
                            if (response.isSuccessful && response.body()?.statusCode == 201) {
                                showLoading(false)
                                Toast.makeText(this@RegisterActivity,"Register Success", Toast.LENGTH_SHORT).show()
                                val toLogin = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(toLogin)
                            } else {
                                showLoading(false)
                                Toast.makeText(this@RegisterActivity,"Register Failed", Toast.LENGTH_SHORT).show()
                                Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<LoginRegisterResponse>, t: Throwable) {
                            showLoading(false)
                            Toast.makeText(this@RegisterActivity,"Register Failed", Toast.LENGTH_SHORT).show()
                            Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                        }
                    })
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }


}