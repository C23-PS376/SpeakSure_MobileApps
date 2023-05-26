package com.example.speaksure_capstone.addthread

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.speaksure_capstone.databinding.ActivityAddThreadBinding
import com.example.speaksure_capstone.databinding.ActivityHomepageBinding

class AddThreadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddThreadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddThreadBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}