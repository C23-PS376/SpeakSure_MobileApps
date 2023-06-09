package com.example.speaksure_capstone.ui.dashboard

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.speaksure_capstone.R
import com.example.speaksure_capstone.databinding.ActivityHomepageBinding
import com.example.speaksure_capstone.ui.login.LoginActivity

class HomepageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomepageBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val mytoken = preferences.getString(LoginActivity.TOKEN, "").toString()
        val isLoggedIn = preferences.getBoolean(LoginActivity.ISLOGGEDIN, false)
        if(mytoken == "" ||!isLoggedIn ){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }




        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setupWithNavController(navController)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_nav -> {
                    navController.navigate(R.id.navigation_home)
                    true
                }
                R.id.add_nav -> {
                    navController.navigate(R.id.navigation_add_thread)
                    true
                }
                R.id.profile_nav -> {
                    navController.navigate(R.id.navigation_profile)
                    true
                }
                else -> false
            }
        }
    }
}