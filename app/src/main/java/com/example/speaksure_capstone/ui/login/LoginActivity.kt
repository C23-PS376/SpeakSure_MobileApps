package com.example.speaksure_capstone.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.speaksure_capstone.R
import com.example.speaksure_capstone.ui.dashboard.HomepageActivity
import com.example.speaksure_capstone.databinding.ActivityLoginBinding
import com.example.speaksure_capstone.network.ApiConfig
import com.example.speaksure_capstone.response.LoginRegisterResponse
import com.example.speaksure_capstone.ui.register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val SHARED_PREFERENCES = "shared_preferences"
        const val USER_ID = "user_id"
        const val TOKEN = "token"
        const val ISLOGGEDIN = "isloggedin"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)

        binding.btnLogin.setOnClickListener(this)
        binding.btnToRegister.setOnClickListener {
            val toRegister = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(toRegister)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_login -> {
                login()
            }
        }
    }

    private fun login() {
        showLoading(true)
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()
        ApiConfig
            .getApiService()
            .login(email, password)
            .enqueue(object : Callback<LoginRegisterResponse> {
                override fun onResponse(
                    call: Call<LoginRegisterResponse>,
                    response: Response<LoginRegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.data?.get(0)?.apply {
                            validateLogin(id,accessToken.toString())
                        }
                        Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT)
                            .show()
                        val mainIntent = Intent(this@LoginActivity, HomepageActivity::class.java)
                        showLoading(false)
                        startActivity(mainIntent)
                        finish()
                    }else{
                        showLoading(false)
                        Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<LoginRegisterResponse>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(this@LoginActivity, "Data Invalid", Toast.LENGTH_SHORT).show()
                }
            })
    }



    private fun validateLogin(userId: String,  token: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(USER_ID, userId)
        editor.putString(TOKEN, token)
        editor.putBoolean(ISLOGGEDIN, true)
        editor.apply()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}