package com.example.speaksure_capstone.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.speaksure_capstone.R
import com.example.speaksure_capstone.databinding.FragmentProfileBinding
import com.example.speaksure_capstone.ui.addthread.AddThreadFragment
import com.example.speaksure_capstone.ui.dashboard.HomeViewModel
import com.example.speaksure_capstone.ui.login.LoginActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddThreadFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
//class ProfileFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_thread, container, false)
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment AddThreadFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            AddThreadFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}

class ProfileFragment : Fragment() {

    private lateinit var preferences: SharedPreferences
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val rootView = binding.root

        preferences = requireActivity().getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        var token = preferences.getString(LoginActivity.TOKEN, "").toString()
        token= "Bearer $token"

        viewModel = ViewModelProvider(this, ProfileViewModel.ProfileViewModelFactory(token, requireContext()))[ProfileViewModel::class.java]

        binding.btnLogout.setOnClickListener {
            clearSession()
            val loginIntent = Intent(activity, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        val isLoggedIn = preferences.getBoolean(LoginActivity.ISLOGGEDIN, false)

        if (!isLoggedIn) {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        return rootView
    }

    private fun clearSession() {
        val sharedPreferences = requireActivity().getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        // TODO: Use the ViewModel
    }

}