package com.example.speaksure_capstone.ui.dashboard

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.speaksure_capstone.adapter.LoadingStateAdapter
import com.example.speaksure_capstone.adapter.ThreadPagingAdapter
import com.example.speaksure_capstone.databinding.FragmentHomeBinding
import com.example.speaksure_capstone.ui.login.LoginActivity

class HomeFragment : Fragment() {
    private lateinit var preferences: SharedPreferences
    private lateinit var viewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val rootView = binding.root

        preferences = requireActivity().getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        var token = preferences.getString(LoginActivity.TOKEN, "").toString()
        token= "Bearer $token"

        binding.rvThread.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this, HomeViewModel.HomeViewModelFactory(token, requireContext()))[HomeViewModel::class.java]

        val isLoggedIn = preferences.getBoolean(LoginActivity.ISLOGGEDIN, false)

        if (!isLoggedIn)  {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }else{
            getData()
        }

        return rootView
    }


    private fun getData() {
        val adapter = ThreadPagingAdapter()
        binding.rvThread.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.thread.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
    }
}
