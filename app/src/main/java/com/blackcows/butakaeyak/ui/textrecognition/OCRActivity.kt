package com.blackcows.butakaeyak.ui.textrecognition

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.blackcows.butakaeyak.databinding.ActivityOcrBinding
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.android.gms.common.annotation.KeepName
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@KeepName
@RequiresApi(VERSION_CODES.LOLLIPOP)
class OCRActivity : AppCompatActivity()  {
    private lateinit var binding : ActivityOcrBinding
    private var imageCapture: ImageCapture? = null

    private val ocrViewModel : OCRViewModel by viewModels()

    private var profileUri: Uri? = null

    private lateinit var cameraExecutor: ExecutorService

    val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build()) //텍스트 인식에 사용될 모델

    //이미지 자르기
    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // returned uri 사용
//            Glide.with(this@OCRActivity)
//                .load(result.uriContent)
//                .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
//                .into(binding.takeImage)

            profileUri = result.uriContent
            runTextRecognition(InputImage.fromFilePath(this@OCRActivity, profileUri?:"".toUri()))
        } else {
            val exception = result.error
        }
    }

    //이미지 가져오기
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                cropImage.launch(
                    CropImageContractOptions(
                        uri = uri, // 크롭할 이미지 uri
                        cropImageOptions = CropImageOptions(
                            outputCompressFormat = Bitmap.CompressFormat.PNG,//사진 확장자 변경
                            // 원하는 옵션 추가
                        )
                    )
                )
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOcrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Set up the listeners for take photo and video capture buttons
        binding.takePhotoBtn.setOnClickListener {
            takePhoto()
            Log.d(TAG, "takePhoto")
        }
        binding.selectPhotoBtn.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            Log.d(TAG, "captureVideo")
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

    }



    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()
            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)
            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.KOREA)
            .format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/prescription-Image")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)

                    output.savedUri?.let { uri ->
                        try {
                            // 저장된 이미지의 URI로부터 InputImage 생성
                            val image = InputImage.fromFilePath(this@OCRActivity, uri)
                            // 텍스트 인식 시작
                            runTextRecognition(image)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(baseContext, "Failed to load image", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        )


    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun runTextRecognition(image: InputImage) {
        binding.takePhotoBtn.setEnabled(false)
        binding.selectPhotoBtn.setEnabled(false)
        recognizer.process(image)
            .addOnSuccessListener { texts ->
                binding.takePhotoBtn.setEnabled(true)
                binding.selectPhotoBtn.setEnabled(true)
                processTextRecognitionResult(texts)
            }
            .addOnFailureListener { e -> // Task failed with an exception
                binding.takePhotoBtn.setEnabled(true)
                binding.selectPhotoBtn.setEnabled(true)
                e.printStackTrace()
            }
    }

    private fun processTextRecognitionResult(texts: Text) {
        Log.d(TAG, "${texts.text}")

        val blocks: List<Text.TextBlock> = texts.textBlocks
        if (blocks.isEmpty()) {
            Toast.makeText(baseContext, "글자가 없어요...😥", Toast.LENGTH_SHORT).show()
            return
        }

        // 인식된 텍스트를 한 줄씩 표시
        val resultText = StringBuilder()
        for (block in blocks) {
            for (line in block.lines) {
                resultText.append(line.text).append("\n")
            }
        }

        // 결과를 TextView에 표시
        lifecycleScope.launch {
            ocrViewModel.uiState.collect{uiState ->
                binding.textViewOcrResult.text = when(uiState){
                    is GPTResultUIState.Loading -> "약 이름 찾는중...🧐"
                    is GPTResultUIState.Success -> uiState.response.gptMessage.trim()
                    is GPTResultUIState.Error -> uiState.errorMessage
                }
                when(uiState){
                    is GPTResultUIState.Loading -> binding.lodingProgress.visibility = View.VISIBLE
                    is GPTResultUIState.Success -> {
                        binding.lodingProgress.visibility = View.GONE
                    }
                    is GPTResultUIState.Error -> {
                        binding.lodingProgress.visibility = View.GONE
                        uiState.errorMessage
                    }
                }
            }
        }
        ocrViewModel.fetchAiAnalysisResult(texts.text)
    }


    companion object {
        private const val TAG = "TextRecognition"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}
