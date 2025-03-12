package com.schwarzit.lovenpark.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.databinding.FragmentCameraBinding
import com.schwarzit.lovenpark.signal.*
import com.schwarzit.lovenpark.utils.Util
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment : Fragment() {
    private var binding: FragmentCameraBinding? = null
    private val cameraViewModel: CameraViewModel by viewModels()
    private val signalRootViewModel: SignalRootViewModel by activityViewModels()
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null
    private var galleryImageAdapter: GalleryImageAdapter? = null
    private var cameraAdapter: CameraImageAdapter? = null
    private var outputDirectory: File? = null
    private var onDeleteCameraPhotoListener: ((photoPosition: Int) -> Unit)? = null

    private val cameraPermissionsResultCallback =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                binding?.apply {
                    rvCameraPhotos.visibility = View.VISIBLE
                    viewFinder.visibility = View.VISIBLE
                }
                startCamera()
            } else {
                cameraViewModel.cameraDenied = true
            }
            passPhotoPermissionsToSignalRootFragment()
        }

    private val requestGalleryPermissionCallback =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { allowedPermission ->
            if (allowedPermission || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestPhotoFromGallery.launch(Util.OPEN_GALLERY_INPUT)
            } else {
                cameraViewModel.galleryDenied = true
            }
            passPhotoPermissionsToSignalRootFragment()
        }

    private val requestPhotoFromGallery =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { allPhotosUris ->
            allPhotosUris.forEach { photoUri ->
                val bitmapProcessor = context?.let { BitmapProcessor(it) }
                val filePath = photoUri.let { bitmapProcessor?.getPath(context, it) }
                val test = context?.let { bitmapProcessor?.localFileExist(filePath, it) }
                if (test == false) {
                    Toast.makeText(
                        context,
                        "Please choose file from the internal memory",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    cameraViewModel.addImage(photoUri)
                    cameraAdapter?.setImageList(cameraViewModel.listUris)
                    updateGalleryButtonImage()
                    passSavedUriToSignalRootFragment()
                    showErrorIfPhotosNumberIsGreaterThanAllowed()
                    setNextButtonState()
                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding?.root
    }

    private fun setCamera() {
        binding?.buttonPhotoAction?.setOnClickListener {
            takePhoto()
            cameraAdapter?.setImageList(cameraViewModel.listUris)
            galleryImageAdapter?.setImageList(cameraViewModel.listUris)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerForUrisFromSignalRootFragment()
        GlobalScope.launch {
            delay(900)
            requestCameraPermissions()
        }
        setCamera()
        updateGalleryButtonImage()
        cameraAdapter = CameraImageAdapter(requireContext())
        setCameraLinearAdapter()
        binding?.apply {
            galleryButton.setOnClickListener {
                requestGalleryPermissionLauncher()
                showErrorIfPhotosNumberIsGreaterThanAllowed()
                it.setBackgroundResource(R.drawable.about_cleaning_park_1);
                updateGalleryButtonImage()
            }

            buttonPhotoAction.apply {
                requestCameraPermissions()
                setOnClickListener {
                    takePhoto()
                    cameraAdapter?.setImageList(cameraViewModel.listUris)
                }
            }
        }

        outputDirectory = getOutputDirectory()
    }

    private fun updateGalleryButtonImage() {
        val size = cameraViewModel.listUris.size
        if (size > 0) {
            context?.let { ctx ->
                val bitmap = BitmapProcessor(ctx).processBitmap(
                    cameraViewModel.listUris[size - 1],
                    ctx.resources.getDimension(R.dimen.gallery_image_required_size).toInt(),
                    true
                )
                binding?.galleryButton?.setImageBitmap(bitmap)
            }
        } else {
            binding?.galleryButton?.setBackgroundResource(R.drawable.about_cleaning_park_1);
        }
    }

    private fun registerForUrisFromSignalRootFragment() {
        val stringUris = arguments?.getStringArrayList(URIS)
        if (stringUris != null) {
            if (stringUris.isNotEmpty()) {
                stringUris.forEach { cameraViewModel.addImage(Uri.parse(it)) }
            }
        }
    }

    /**Grants CAMERA permissions*/
    private fun requestCameraPermissions() {
        val permissionCamera = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionsResultCallback.launch(Manifest.permission.CAMERA)
        } else {
            startCamera()
        }
    }

    private fun requestGalleryPermissionLauncher() {
        requestGalleryPermissionCallback
            .launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun setCameraLinearAdapter() {
        val recyclerView = binding?.rvCameraPhotos
        recyclerView?.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        cameraAdapter?.setImageList(cameraViewModel.listUris)
        recyclerView?.adapter = cameraAdapter

        cameraAdapter?.onDeleteImageListener = {
            onDeleteCameraPhotoListener?.invoke(it)
            cameraViewModel.listUris.removeAt(it)
            updateGalleryButtonImage()
            setNextButtonState()
            passSavedUriToSignalRootFragment()
            cameraAdapter?.setImageList(cameraViewModel.listUris)
            showErrorIfPhotosNumberIsGreaterThanAllowed()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            imageCapture = ImageCapture.Builder().build()
            val viewFinder = binding?.viewFinder
            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
                preview.setSurfaceProvider(viewFinder?.surfaceProvider)
            } catch (exc: Exception) {
                Log.e(CameraFragment::class.simpleName, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @SuppressLint("SimpleDateFormat")
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory, LP_SIGNAL + SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + FILE_EXTENSION
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {

                override fun onError(exc: ImageCaptureException) {
                    Log.e(
                        CameraFragment::class.simpleName,
                        "Photo capture failed: ${exc.message}",
                        exc
                    )
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    showErrorIfPhotosNumberIsGreaterThanAllowed()
                    cameraViewModel.addImage(savedUri)
                    cameraAdapter?.setImageList(cameraViewModel.listUris)
                    galleryImageAdapter?.setImageList(cameraViewModel.listUris)
                    updateGalleryButtonImage()
                    passSavedUriToSignalRootFragment()
                    setNextButtonState()
                }
            })
    }

    private fun showErrorIfPhotosNumberIsGreaterThanAllowed() {
        cameraViewModel.apply {
            checkIfPhotoNumberReachedMax()
            imageNumberEqualOrHigherThanMaxNumberLiveData.observe(viewLifecycleOwner)
            { imageNumberEqualOrHigherThanMaxNumber ->
                binding?.apply {
                    if (imageNumberEqualOrHigherThanMaxNumber) {
                        val string = requireContext().getString(
                            (R.string.files_number_warning),
                            PHOTOS_MAX_NUMBER
                        )
                        tvWarningFilesNumber.text = string
                        cvMaxFilesWarning.visibility = View.VISIBLE
                    } else {
                        cvMaxFilesWarning.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setNextButtonState() {
        cameraViewModel.checkPhotoMinNumber()
        cameraViewModel.imageNumberEqualOrHigherThanMinNumberLiveData.observe(viewLifecycleOwner) {
            signalRootViewModel.setNextButtonState(it)
        }
    }

    private fun passSavedUriToSignalRootFragment() {
        passPhotoDataToSignalRootFragment()
    }

    private fun passPhotoDataToSignalRootFragment() {
        setFragmentResult(REQUEST_PHOTO_URIS, bundleOf(PHOTO_URIS to cameraViewModel.listUris))
    }

    private fun passPhotoPermissionsToSignalRootFragment() {
        val permissionsDenied = cameraViewModel.cameraDenied && cameraViewModel.galleryDenied
        setFragmentResult(
            REQUEST_PHOTO_PERMISSIONS,
            bundleOf(PHOTO_PERMISSIONS to permissionsDenied)
        )
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else requireActivity().filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        camera = null
    }
}