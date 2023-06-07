package com.example.speaksure_capstone.ui.addthread

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.speaksure_capstone.R
import com.example.speaksure_capstone.databinding.FragmentAddThreadBinding
import com.example.speaksure_capstone.ui.login.LoginActivity
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private const val CAMERA_REQUEST_CODE = 1
private const val GALLERY_REQUEST_CODE = 2
private const val RECORD_AUDIO_REQUEST_CODE =3
private const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE =4
private const val PICK_AUDIO_REQUEST_CODE = 5
private const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 6

private const val FILENAME_FORMAT = "dd-MMM-yyyy"
private const val MAXIMAL_SIZE = 1000000
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

    private var getImageFile: File? = null
    private var getAudioFile: File? = null

    private val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    private lateinit var preferences: SharedPreferences

    private lateinit var viewModel: AddThreadViewModel




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddThreadBinding.inflate(inflater, container, false)
        val rootView = binding.root

        viewModel = ViewModelProvider(this)[AddThreadViewModel::class.java]


        preferences = requireActivity().getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        permission()

        // Inisialisasi TextInputEditText
        textInputEditText = binding.textInput
        val imageView = binding.imageView

        imageView.setOnClickListener {
            showImageSourceOptions()
        }
        binding.btnUpload.setOnClickListener{
            Log.e("btn","btn dipencet")
            uploadThread()
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

        getAudioFile=File(outputFilePath.toString())

        binding.btnRecord.setOnClickListener {
            isPlaying=false
            Log.d("LOG_TAG", "btnRecord clicked")
            if (isPlaying) {
                Log.e("LOG_TAG", "masuk ke stopPLaying")
                stopPlaying()
            } else {  // Add this condition to prevent automatic startPlaying() when recording is stopped
                Log.e("LOG_TAG", "masuk ke startPLaying")
                startPlaying(outputFilePath.toString())
            }
        }
    }



    private fun startPlaying(outputFilePath : String) {
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

    private fun getTitleText(): String {
        return textInputEditText.text.toString().takeIf { it.isNotBlank() } ?: "No description provided"
    }
    private fun getDescText(): String {
        return textInputEditText.text.toString().takeIf { it.isNotBlank() } ?: "No description provided"
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

    private fun uploadThread(){
        Log.e("btn","btn dipencet")
        Log.e("audio", getAudioFile.toString())
        Log.e("btn",getImageFile.toString())
        if(getTitleText()!= null){

            val audioFile = getAudioFile
            val imageFile = reduceFileImage(getImageFile as File)

            preferences = requireActivity().getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
            var token = preferences.getString(LoginActivity.TOKEN, "").toString()
            token= "Bearer $token"

            val title = getTitleText().toRequestBody("text/plain".toMediaType())
            val description = getDescText().toRequestBody("text/plain".toMediaType())
            val topic = "2".toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val requestAudioFile = audioFile!!.asRequestBody("audio/mp3".toMediaType())

            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )
            val audioMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "audio",
                audioFile.name,
                requestAudioFile
            )
            Log.e("token", token)
            viewModel.addThread(token,title, description,topic,imageMultipart,audioMultipart)

        }

    }

    private fun uriToFile(selectedUri: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedUri) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    private fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)

        var compressQuality = 100
        var streamLength: Int

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)

        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

        return file
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    // Gambar diambil dari kamera
                    @Suppress("DEPRECATION")
                    val imageBitmap = data?.extras?.get("data") as Bitmap?
                    getImageFile= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        data?.getSerializableExtra("picture", File::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        data?.getSerializableExtra("picture")
                    } as? File

                    // Gunakan gambar yang diambil
                    handleImage(imageBitmap)
                }
                GALLERY_REQUEST_CODE -> {
                    // Gambar dipilih dari galeri
                    val imageUri = data?.data
                    getImageFile = uriToFile(imageUri!!,this.requireContext())
                    // Ambil gambar dari URI
                    val imageBitmap = getImageBitmapFromUri(imageUri)
                    // Gunakan gambar yang dipilih
                    handleImage(imageBitmap)
                }
            }
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

    private fun permission(){
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Jika izin belum diberikan, minta izin RECORD_AUDIO secara dinamis
            @Suppress("DEPRECATION")
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_REQUEST_CODE)
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            @Suppress("DEPRECATION")
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            @Suppress("DEPRECATION")
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PICK_AUDIO_REQUEST_CODE)
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            @Suppress("DEPRECATION")
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_REQUEST_CODE)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
