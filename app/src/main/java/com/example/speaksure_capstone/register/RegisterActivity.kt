package com.example.speaksure_capstone.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import com.example.speaksure_capstone.R
import com.example.speaksure_capstone.dashboard.HomepageActivity
import com.example.speaksure_capstone.databinding.ActivityRegisterBinding
import com.example.speaksure_capstone.network.ApiConfig
import com.example.speaksure_capstone.response.RegisterResponse
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
            val toLogin = Intent(this@RegisterActivity, HomepageActivity::class.java)
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
                    val client = ApiConfig.getApiService().register(email, name, password)
                    client.enqueue(object : Callback<RegisterResponse> {
                        override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                            if (response.isSuccessful && response.body()?.statusCode == 201) {
                                Toast.makeText(this@RegisterActivity,"success", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@RegisterActivity,"error", Toast.LENGTH_SHORT).show()
                                Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                            Toast.makeText(this@RegisterActivity,t.message, Toast.LENGTH_SHORT).show()
                            Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                        }
                    })
                }
            }
        }
    }


}