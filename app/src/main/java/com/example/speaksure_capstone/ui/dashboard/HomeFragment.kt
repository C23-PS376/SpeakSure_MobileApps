package com.example.speaksure_capstone.ui.dashboard

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
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
        var mytoken = preferences.getString(LoginActivity.TOKEN, "").toString()
        if(mytoken == null){
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        var token = "Bearer $mytoken"
        val isLoggedIn = preferences.getBoolean(LoginActivity.ISLOGGEDIN, false)


        viewModel = ViewModelProvider(this, HomeViewModel.HomeViewModelFactory(query,token, requireContext()))[HomeViewModel::class.java]
        if (!isLoggedIn)  {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }else{
            getData()
        }
        binding.rvThread.layoutManager = LinearLayoutManager(requireContext())
        adapter = ThreadPagingAdapter()
        binding.rvThread.adapter = adapter
        searchView = binding.search

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(queryIn: String): Boolean {
                query = queryIn
                searchData(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                query = newText
                searchData(query)
                return true
            }
        })

        filterTopic()


        return rootView
    }



    private fun filterTopic(){

        adapter = ThreadPagingAdapter()
        binding.rvThread.adapter = adapter

        // Set filter chip

        binding.homeChipGroup.forEach { chip ->
            chip.setOnClickListener {
                when (chip.id){
                    R.id.all_topic->{
                        val chipFilter = binding.allTopic
                        adapter.setFilterChip(chipFilter)

                        // Set chip click listener
                        val query = chipFilter.text.toString()
                        adapter.filterData(query)
                        Log.e("chip",query)
                    }
                    R.id.topic_1 -> {
                        val chipFilter = binding.topic1
                        adapter.setFilterChip(chipFilter)

                        // Set chip click listener
                        val query = chipFilter.text.toString()
                        adapter.filterData(query)
                        Log.e("chip",query)



                        // Aksi untuk Topic 1 dipilih
                    }
                    R.id.topic_2 -> {
                        val chipFilter = binding.topic2
                        adapter.setFilterChip(chipFilter)


                        // Set chip click listener

                            val query = chipFilter.text.toString()
                            adapter.filterData(query)
                        Log.e("chip",query)

                        // Aksi untuk Topic 2 dipilih
                    }
                    R.id.topic_3 -> {
                        val chipFilter = binding.topic3
                        adapter.setFilterChip(chipFilter)

                        // Set chip click listener
                            val query = chipFilter.text.toString()
                            adapter.filterData(query)
                        Log.e("chip",query)

                        // Aksi untuk Topic 3 dipilih
                    }
                    R.id.topic_4 -> {
                        val chipFilter = binding.topic4
                        adapter.setFilterChip(chipFilter)

                        // Set chip click listener

                            val query = chipFilter.text.toString()
                            adapter.filterData(query)
                        Log.e("chip",query)

                        // Aksi untuk Topic 4 dipilih
                    }
                    R.id.topic_5 -> {
                        val chipFilter = binding.topic5
                        adapter.setFilterChip(chipFilter)

                        // Set chip click listener

                            val query = chipFilter.text.toString()
                            adapter.filterData(query)
                        Log.e("chip",query)

                        // Aksi untuk Topic 5 dipilih
                    }
                }
            }
        }
    }



    private fun searchData(query:String) {
        val adapter = ThreadPagingAdapter()
        binding.rvThread.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.searchThreads(query).observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun getData() {
        val adapter = ThreadPagingAdapter()
        binding.rvThread.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.getThread().observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
            if (isSearching) {
                binding.rvThread.scrollToPosition(0)
                isSearching = false
            }
        }
    }
}
