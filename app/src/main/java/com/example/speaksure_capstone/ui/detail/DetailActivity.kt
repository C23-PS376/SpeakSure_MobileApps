package com.example.speaksure_capstone.ui.detail

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.speaksure_capstone.R
import com.example.speaksure_capstone.databinding.ActivityDetailBinding
import com.example.speaksure_capstone.network.ApiConfig
import com.example.speaksure_capstone.response.DetailResponse
import com.example.speaksure_capstone.response.LikeResponse
import com.example.speaksure_capstone.ui.login.LoginActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.sql.Timestamp
import java.util.*

class DetailActivity : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var binding: ActivityDetailBinding
    private var isPlaying = false
    private lateinit var viewModel: DetailViewModel
    private var mediaPlayer: MediaPlayer? = null
    private var outputFilePath: String? = null
    @Suppress("DEPRECATION")
    private val recorder = MediaRecorder()
    private var getAudioFile: File? = null
    private var isRecording = false

    companion object {
        const val ID_THREAD = "id_thread"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = this.getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        var token = preferences.getString(LoginActivity.TOKEN, "").toString()
        token= "Bearer $token"

        if(token == null){
            val intent = Intent(this@DetailActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        val id = intent.getStringExtra(ID_THREAD).toString()
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        val detailThread = intent.getStringExtra(ID_THREAD)

        if (detailThread!= null) {
            viewModel.getDetail(token,id)
        }
        viewModel.detailThread.observe(this) { detailThreads ->
            setDetailThread(detailThreads)
        }

        viewModel.commentThread.observe(this){

        }

        binding.btnSend.setOnClickListener {
            sendComment()
        }

        mediaPlayer = MediaPlayer()

        initializeComment()

    }

    private fun sendComment(){
        val audioFile = getAudioFile as File

        preferences = this.getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        var token = preferences.getString(LoginActivity.TOKEN, "").toString()
        token= "Bearer $token"

        val threadId = setThreadId().toString().toRequestBody("text/plain".toMediaType())
        val comment = setCommentText().toRequestBody("text/plain".toMediaType())
        val requestAudioFile =getAudioFile!!.asRequestBody("audio/mp3".toMediaType())
        val audioMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "audio",
            audioFile.name,
            requestAudioFile
        )

        viewModel.setComment(token,threadId,comment,audioMultipart)
    }

    private fun initializeComment(){
        setupAudioRecorder()
        setCommentText()

        binding.btnMic.setOnClickListener {
            if (isRecording) {
                stopRecording()
            } else {
                startRecording()
            }
        }
    }

    private fun startRecording() {
        try {
            recorder.prepare()
            recorder.start()
            isRecording = true
            // Ubah tampilan tombol saat sedang merekam
            binding.btnMic.setImageResource(R.drawable.baseline_stop_24)
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle error saat gagal memulai rekaman
        }
    }

    private fun stopRecording() {
        recorder.stop()
        binding.btnMic.setImageResource(R.drawable.baseline_mic_24)
        isRecording = false

        getAudioFile= File(outputFilePath.toString())


    }

    private fun setCommentText(): String {
        return binding.commentET.text.toString()
    }

    private fun setThreadId(): Int {
        return intent.getStringExtra(ID_THREAD)!!.toInt()
    }

    private fun setupAudioRecorder() {
        deleteRecordedFile()
        outputFilePath = this.externalCacheDir?.absolutePath + "/recorded_audio.3gp" // Perbarui outputFilePath
        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(outputFilePath)
        }
    }

    private fun deleteRecordedFile() {
        val file = File(outputFilePath.toString())
        if (file.exists()) {
            val deleted = file.delete()
            if (deleted) {
                Log.d("LOG_TAG", "File deleted successfully")
                initializeComment()
            } else {
                Log.d("LOG_TAG", "Failed to delete file")
            }
        } else {
            Log.d("LOG_TAG", "File does not exist")
        }
    }

    private fun setDetailThread(detail: DetailResponse) {
        var likesCount: Int = detail.data?.likesCount!!.toInt()
        binding.Name.text = detail.data?.user?.name
        val timeStamp = detail.data?.createdAt?.let { Timestamp(it.toLong()) }
        binding.dateThread.text = timeStamp.toString()
        binding.description.text = detail.data?.description
        binding.title.text = detail.data?.title
        binding.tvJlhLike.text = detail.data?.likesCount
        binding.tvJlhComment.text = detail.data?.commentsCount
        binding.btnPlayThread.text = detail.data?.audioLength
        Glide.with(this@DetailActivity)
            .load(detail.data?.image)
            .into(binding.ivThread)
        binding.btnPlayThread.setOnClickListener {
            if(!isPlaying){
                playAudio(detail.data?.audio.toString())
            }
            else{
                pauseAudio()
            }
        }
        binding.btnUp.setOnClickListener {
            preferences =binding.root.context.getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
            var token = preferences.getString(LoginActivity.TOKEN, "").toString()
            token= "Bearer $token"
            val id = detail?.data.id.toString()
            val client = ApiConfig.getApiService().like(token,id)
            client.enqueue(object : Callback<LikeResponse> {
                override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                    if (response.isSuccessful && response.body()?.statusCode == 201) {
                        Toast.makeText(binding.root.context,"success", Toast.LENGTH_SHORT).show()
                        likesCount++ // Tambahkan 1 ke likesCount
                        binding.tvJlhLike.text = likesCount.toString()
                    } else {
                        Toast.makeText(binding.root.context,"Thread already liked", Toast.LENGTH_SHORT).show()
                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                    Toast.makeText(binding.root.context,t.message, Toast.LENGTH_SHORT).show()
                    Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                }
            })
        }
        binding.btnDown.setOnClickListener {
            preferences =binding.root.context.getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
            var token = preferences.getString(LoginActivity.TOKEN, "").toString()
            token= "Bearer $token"
            val id = detail?.data.id.toString()
            val client = ApiConfig.getApiService().unlike(token,id)
            client.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        Toast.makeText(binding.root.context,"success", Toast.LENGTH_SHORT).show()
                        likesCount-- // Tambahkan 1 ke likesCount
                        binding.tvJlhLike.text = likesCount.toString()
                    } else {
                        Toast.makeText(binding.root.context,"Thread already unliked", Toast.LENGTH_SHORT).show()
                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(binding.root.context,t.message, Toast.LENGTH_SHORT).show()
                    Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                }
            })
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
                Toast.makeText(this@DetailActivity, "Playing Audio", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: IOException) {
            }
        }
    }

    private fun pauseAudio(){
        isPlaying =false
        mediaPlayer?.release()
        mediaPlayer = null
        Toast.makeText(this@DetailActivity, "Stop Audio", Toast.LENGTH_SHORT)
            .show()
    }
}