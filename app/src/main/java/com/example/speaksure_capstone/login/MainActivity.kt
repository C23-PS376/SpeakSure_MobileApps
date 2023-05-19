package com.example.speaksure_capstone.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.speaksure_capstone.R
import com.example.speaksure_capstone.databinding.ActivityMainBinding
import com.example.speaksure_capstone.register.RegisterActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnToRegister.setOnClickListener{
            val toRegister = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(toRegister)
        }
    }
}