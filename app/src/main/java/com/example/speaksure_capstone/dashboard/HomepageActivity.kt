package com.example.speaksure_capstone.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.speaksure_capstone.R
import com.example.speaksure_capstone.addthread.AddThreadActivity
import com.example.speaksure_capstone.databinding.ActivityHomepageBinding
import com.example.speaksure_capstone.login.MainActivity
import com.example.speaksure_capstone.profile.ProfileActivity

class HomepageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController


        val bottomNavigationView = binding.bottomNavigation

        bottomNavigationView.setupWithNavController(navController)*/

        binding.bottomNavigation.setOnItemSelectedListener{ item ->
            when(item.itemId) {
                R.id.home_nav -> {
                    /*navController.navigate(R.id.navigation_home)*/
                    val homeIntent = Intent(this@HomepageActivity, HomeActivity::class.java)
                    startActivity(homeIntent)
                    overridePendingTransition(0,0)
                    true
                }
                R.id.add_nav -> {
                    /*navController.navigate(R.id.navigation_add_thread)*/
                    val addIntent = Intent(this@HomepageActivity, AddThreadActivity::class.java)
                    startActivity(addIntent)
                    true
                }
                R.id.profile_nav -> {
                    /*navController.navigate(R.id.navigation_profile)*/
                    val profileIntent = Intent(this@HomepageActivity, ProfileActivity::class.java)
                    startActivity(profileIntent)
                    true
                }
                else -> false
            }
        }
    }

}