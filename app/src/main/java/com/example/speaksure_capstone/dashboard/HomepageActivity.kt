package com.example.speaksure_capstone.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.speaksure_capstone.R
import com.example.speaksure_capstone.databinding.ActivityHomepageBinding

class HomepageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*val fragmentManager =supportFragmentManager
// Buat instance HomeFragment
        val homeFragment = HomeFragment()

// Cek apakah fragment sudah ada sebelumnya
        val fragment = fragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)
        if (fragment !is HomeFragment) {
            // Jika fragment belum ada, tambahkan menggunakan childFragmentManager dari container yang sesuai dalam binding
            fragmentManager.beginTransaction()
                .add(binding.frameContainer.id, homeFragment, HomeFragment::class.java.simpleName)
                .commit()
        }*/

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController


        val bottomNavigationView = binding.bottomNavigation

        bottomNavigationView.setupWithNavController(navController)

        binding.bottomNavigation.setOnItemSelectedListener{ item ->
            when(item.itemId) {
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