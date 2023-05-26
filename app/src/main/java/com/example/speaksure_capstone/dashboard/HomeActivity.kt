package com.example.speaksure_capstone.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.speaksure_capstone.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}