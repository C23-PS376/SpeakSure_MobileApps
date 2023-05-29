package com.example.speaksure_capstone.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.speaksure_capstone.ui.dashboard.HomepageActivity
import com.example.speaksure_capstone.databinding.ActivityMainBinding
import com.example.speaksure_capstone.ui.register.RegisterActivity

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
        binding.btnToHomepage.setOnClickListener{
            val toRegister = Intent(this@MainActivity, HomepageActivity::class.java)
            startActivity(toRegister)
        }
    }
}