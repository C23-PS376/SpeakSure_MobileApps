package com.example.speaksure_capstone.ui.addthread

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.speaksure_capstone.R
import com.example.speaksure_capstone.databinding.FragmentAddThreadBinding
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

private const val CAMERA_REQUEST_CODE = 1
private const val GALLERY_REQUEST_CODE = 2
private const val RECORD_AUDIO_REQUEST_CODE =3
private const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE =4

class AddThreadFragment : Fragment() {
    private lateinit var textInputEditText: TextInputEditText
    private var _binding: FragmentAddThreadBinding? = null
    private val binding get() = _binding!!
    private var isRecording = false
    @Suppress("DEPRECATION")
    private val recorder = MediaRecorder()
    private var outputFilePath: String? = null

    private var player: MediaPlayer? = null
    private var isPlaying = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddThreadBinding.inflate(inflater, container, false)
        val rootView = binding.root

        // Inisialisasi TextInputEditText
        textInputEditText = binding.textInput
        val imageView = binding.imageView

        imageView.setOnClickListener {
            showImageSourceOptions()
        }


        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Jika izin belum diberikan, minta izin RECORD_AUDIO secara dinamis
            @Suppress("DEPRECATION")
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_REQUEST_CODE)
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Jika izin belum diberikan, minta izin WRITE_EXTERNAL_STORAGE secara dinamis
            @Suppress("DEPRECATION")
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
        }




        initializeFragment()


        return rootView
    }

    private fun initializeFragment() {
        setupAudioRecorder()

        binding.btnRecord.setOnClickListener {
            if (isRecording) {
                stopRecording()
            } else {
                startRecording()
            }
        }

        binding.iconClose.setOnClickListener {
            deleteRecordedFile()
            resetButtonState()
        }
    }



    private fun setupAudioRecorder() {
        deleteRecordedFile()
        outputFilePath = requireContext().externalCacheDir?.absolutePath + "/recorded_audio.3gp" // Perbarui outputFilePath
        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(outputFilePath)
        }
    }

    private fun startRecording() {
        try {
            recorder.prepare()
            recorder.start()
            isRecording = true
            // Ubah tampilan tombol saat sedang merekam
            binding.btnRecord.setText(R.string.stop_record)
            binding.btnRecord.setIconResource(R.drawable.baseline_stop_24)
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle error saat gagal memulai rekaman
        }
    }

    private fun stopRecording() {
        recorder.stop()
        isRecording = false
        binding.btnRecord.setText(R.string.play)
        binding.btnRecord.setIconResource(R.drawable.baseline_play_arrow_24)
        binding.fileBox.visibility = View.VISIBLE

        Log.d("LOG_TAG", "Stop recording called")
        Log.d("LOG_TAG", "isPlaying: $isPlaying")

        binding.btnRecord.setOnClickListener {
            isPlaying=false
            Log.d("LOG_TAG", "btnRecord clicked")
            if (isPlaying) {
                Log.e("LOG_TAG", "masuk ke stopPLaying")
                stopPlaying()
            } else {  // Add this condition to prevent automatic startPlaying() when recording is stopped
                Log.e("LOG_TAG", "masuk ke startPLaying")
                startPlaying()
            }
        }
    }

    private fun startPlaying() {
        isPlaying =true
        releaseMediaPlayer()
        player = MediaPlayer().apply {
            try {
                setDataSource(outputFilePath)
                prepare()
                Log.d("LOG_TAG", "MediaPlayer prepared")
                start()
                Log.d("LOG_TAG", "MediaPlayer started")
            } catch (e: IOException) {
                Log.e("LOG_TAG", "$e failed diisni")
            }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }


    private fun releaseMediaPlayer() {
        player?.apply {
            stop()
            release()
        }
        player = null
    }

    private fun resetButtonState() {
        isRecording = false
        recorder.reset()
        setupAudioRecorder()
        binding.btnRecord.setText(R.string.record)
        binding.btnRecord.setIconResource(R.drawable.baseline_mic_24)
        binding.fileBox.visibility = View.GONE
        releaseMediaPlayer()
    }

    private fun deleteRecordedFile() {
        val file = File(outputFilePath.toString())
        if (file.exists()) {
            val deleted = file.delete()
            if (deleted) {
                Log.d("LOG_TAG", "File deleted successfully")
                initializeFragment()
            } else {
                Log.d("LOG_TAG", "Failed to delete file")
            }
        } else {
            Log.d("LOG_TAG", "File does not exist")
        }
    }

    // Method untuk mendapatkan teks dari TextInputEditText
    private fun getEnteredText(): String {
        return textInputEditText.text.toString()
    }

    private fun showImageSourceOptions() {
        val options = arrayOf("Kamera", "Galeri")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pilih Sumber Gambar")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> {
                    // Pilihan Kamera dipilih
                    openCamera()
                }
                1 -> {
                    // Pilihan Galeri dipilih
                    openGallery()
                }
            }
        }
        builder.show()
    }

    @Suppress("DEPRECATION")
    private fun openCamera() {
        // Intent untuk membuka kamera
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun openGallery() {
        // Intent untuk membuka galeri
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        @Suppress("DEPRECATION")
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    // Gambar diambil dari kamera
                    @Suppress("DEPRECATION") val imageBitmap = data?.extras?.get("data") as Bitmap?
                    // Gunakan gambar yang diambil
                    handleImage(imageBitmap)
                }
                GALLERY_REQUEST_CODE -> {
                    // Gambar dipilih dari galeri
                    val imageUri = data?.data
                    // Ambil gambar dari URI
                    val imageBitmap = getImageBitmapFromUri(imageUri)
                    // Gunakan gambar yang dipilih
                    handleImage(imageBitmap)
                }
            }
        }
    }

    private fun handleImage(imageBitmap: Bitmap?) {
        // Lakukan sesuatu dengan gambar (misalnya, tampilkan gambar di ImageView)
        binding.imageView.setImageBitmap(imageBitmap)
    }

    private fun getImageBitmapFromUri(imageUri: Uri?): Bitmap? {
        // Konversi URI menjadi Bitmap
        return try {
            val inputStream = imageUri?.let { requireContext().contentResolver.openInputStream(it) }
            BitmapFactory.decodeStream(inputStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }


    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, panggil kembali setupAudioRecorder()
                setupAudioRecorder()
            } else {
                // Izin ditolak, tangani kasus ketika pengguna tidak memberikan izin
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
