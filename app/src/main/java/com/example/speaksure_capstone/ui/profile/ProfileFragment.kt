package com.example.speaksure_capstone.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.speaksure_capstone.R
import com.example.speaksure_capstone.databinding.FragmentProfileBinding
import com.example.speaksure_capstone.response.ProfileResponse
import com.example.speaksure_capstone.ui.login.LoginActivity
import java.io.IOException


class ProfileFragment : Fragment() {

    private lateinit var preferences: SharedPreferences
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var isPlaying = false
    private var mediaPlayer: MediaPlayer? = null

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

        var query =""
        viewModel = ViewModelProvider(this, ProfileViewModel.ProfileViewModelFactory(query,token))[ProfileViewModel::class.java]

        val id = preferences.getString(LoginActivity.USER_ID,"").toString()
        viewModel.getUser(token, id)
        viewModel.detailUser.observe(viewLifecycleOwner) { detailUsers ->
            showLoading(true)
            setDetailUser(detailUsers)
            showLoading(false)
        }
        if(token == null){
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

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
        mediaPlayer = MediaPlayer()


        binding.btnPlayThread.setOnClickListener {


        }
        return rootView
    }

    private fun clearSession() {
        val sharedPreferences = requireActivity().getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    private fun setDetailUser(profile: ProfileResponse) {
        binding.Name.text = profile.data?.name
        binding.tvJlhThread.text = getString(R.string._1_s_thread,profile.data?.threadsCount)
        binding.tvJlhComment.text = getString(R.string._1_s_comment,profile.data?.commentsCount)
        binding.tvEmail.text = getString(R.string._1_s_email,profile.data?.email)
        binding.tvIntroduction.text = getString(R.string._1_s_greeting,profile.data?.name)
        binding.btnPlayThread.text = profile.data?.audioLength
        binding.btnPlayThread.setOnClickListener {
            if(!isPlaying){
                playAudio(profile.data?.audio.toString())
            }
            else{
                pauseAudio()
            }
        }
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }

    private fun playAudio(data:String){
        isPlaying =true
        releaseMediaPlayer()
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(data)
                prepare()
                start()
                Toast.makeText(activity, "Playing Audio", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: IOException) {
            }
        }
    }

    private fun pauseAudio(){
        isPlaying =false
        mediaPlayer?.release()
        mediaPlayer = null
        Toast.makeText(activity, "Stop Audio", Toast.LENGTH_SHORT)
            .show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

}