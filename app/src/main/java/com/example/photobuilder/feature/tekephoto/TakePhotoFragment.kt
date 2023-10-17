package com.example.photobuilder.feature.tekephoto

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.photobuilder.R
import com.example.photobuilder.core.extension.observeWithLifecycle
import com.example.photobuilder.core.extension.toast
import com.example.photobuilder.databinding.FragmentTakePhotoBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class TakePhotoFragment : Fragment(R.layout.fragment_take_photo) {

    private val viewBinding: FragmentTakePhotoBinding by viewBinding(FragmentTakePhotoBinding::bind)
    private val viewModel by viewModels<TakePhotoViewModel>()
    private var imageCapture: ImageCapture? = null
    private lateinit var autoCompleteAdapter: ArrayAdapter<String>

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                toast("Permission request denied")
            } else {
                startCamera()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (allPermissionsGranted()) {
            startCamera()
            setupListeners()
            setupAdapter()
            observeViewState()
            viewBinding.enterTag.setAdapter(autoCompleteAdapter)
            viewBinding.enterTag.threshold = 0
        } else {
            requestPermissions()
        }
    }

    private fun setupAdapter() {
        autoCompleteAdapter =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.drop_down_item,
                R.id.drop_item_view,
            )
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all { permission ->
        ContextCompat.checkSelfPermission(
            requireActivity().baseContext,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun observeViewState() {
        viewModel.uiEffect.observeWithLifecycle(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        viewModel.uiState.observeWithLifecycle(viewLifecycleOwner) { state ->
            viewBinding.saveTagPanel.isVisible = state.isPhotoLoaded
            autoCompleteAdapter.clear()
            autoCompleteAdapter.addAll(state.existingTags.toList())
        }
    }

    private fun setupListeners() {
        viewBinding.takePhotoBtn.setOnClickListener { takePhoto() }
        viewBinding.saveTagButton.setOnClickListener {
            viewModel.savePhoto(viewBinding.enterTag.text.toString())
        }
        viewBinding.backToGalleryButton.setOnClickListener { findNavController().popBackStack() }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.preview.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, exc.toString())
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val timeStamp = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, timeStamp)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                requireContext().contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    output.savedUri?.let { uri ->
                        viewModel.onImageProcessed(
                            uri = uri.toString(),
                            createdAt = timeStamp,
                            tag = "",
                        )
                    }
                }
            }
        )
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private companion object {
        const val TAG = "CameraXApp"
        const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss"
        val REQUIRED_PERMISSIONS =
            mutableListOf(Manifest.permission.CAMERA)
                .apply {
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                        add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }.toTypedArray()
    }
}
