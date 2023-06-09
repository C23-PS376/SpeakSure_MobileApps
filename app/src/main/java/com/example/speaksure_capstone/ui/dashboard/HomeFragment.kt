package com.example.speaksure_capstone.ui.dashboard

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.speaksure_capstone.R
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
    private var query: String = ""
    private lateinit var adapter: ThreadPagingAdapter
    private var isSearching = false
    private var previousQuery = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val rootView = binding.root

        preferences = requireActivity().getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val mytoken = preferences.getString(LoginActivity.TOKEN, "").toString()
        if (mytoken.isEmpty()) {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        val token = "Bearer $mytoken"
        val isLoggedIn = preferences.getBoolean(LoginActivity.ISLOGGEDIN, false)

        binding.rvThread.layoutManager = LinearLayoutManager(requireContext())
        adapter = ThreadPagingAdapter()
        binding.rvThread.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        val searchManager = requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = SearchView(requireContext())
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = "Search"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(queryIn: String): Boolean {
                query = queryIn
                refreshData()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                query = newText
                refreshData()
                return true
            }
        })

        viewModel = ViewModelProvider(this, HomeViewModel.HomeViewModelFactory(query, token, requireContext()))[HomeViewModel::class.java]

        if (!isLoggedIn) {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        } else {
            getData()
        }

        filterTopic()


        return rootView
    }

    private fun filterTopic(){
        binding.homeChipGroup.forEach { chip ->
            chip.setOnClickListener {
                when (chip.id){
                    R.id.topic_1 -> {
                        // Aksi untuk Topic 1 dipilih
                    }
                    R.id.topic_2 -> {
                        // Aksi untuk Topic 2 dipilih
                    }
                    R.id.topic_3 -> {
                        // Aksi untuk Topic 3 dipilih
                    }
                    R.id.topic_4 -> {
                        // Aksi untuk Topic 4 dipilih
                    }
                    R.id.topic_5 -> {
                        // Aksi untuk Topic 5 dipilih
                    }
                }
            }
        }
    }

    private fun refreshData() {
        if (query != previousQuery) {
            isSearching = true
            previousQuery = query
            getData()
        }
    }

    private fun getData() {
        viewModel.setQuery(query)
        viewModel.thread.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
            if (isSearching) {
                binding.rvThread.scrollToPosition(0)
                isSearching = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
