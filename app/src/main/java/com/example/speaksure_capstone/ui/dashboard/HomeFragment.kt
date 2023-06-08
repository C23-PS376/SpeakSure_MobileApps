package com.example.speaksure_capstone.ui.dashboard

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
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
    private lateinit var searchView: SearchView
    private var query:String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val rootView = binding.root

        preferences = requireActivity().getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        var mytoken = preferences.getString(LoginActivity.TOKEN, "").toString()
        if(mytoken == null){
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
        var token= "Bearer $mytoken"

        val isLoggedIn = preferences.getBoolean(LoginActivity.ISLOGGEDIN, false)


        binding.rvThread.layoutManager = LinearLayoutManager(requireContext())

        val searchManager = requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = SearchView(requireContext())
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = "Search"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(queryIn: String): Boolean {
                // Panggil metode untuk melakukan pencarian berdasarkan teks yang dimasukkan
                var tes = queryIn
                getData()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Jika Anda ingin merespons perubahan teks saat pengguna mengetik
                var tes = newText
                getData()
                return true
            }
        })

        viewModel = ViewModelProvider(this, HomeViewModel.HomeViewModelFactory(query,token, requireContext()))[HomeViewModel::class.java]
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
