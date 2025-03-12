package com.schwarzit.lovenpark.signal

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.CountDownTimer
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.camera.BitmapProcessor
import com.schwarzit.lovenpark.data.utils.UserSharedPrefHelper
import com.schwarzit.lovenpark.dialogs.DialogProvider
import com.schwarzit.lovenpark.isUserLocationInPark
import com.schwarzit.lovenpark.keyStore.KeyStoreManager
import com.schwarzit.lovenpark.profile.signals.data.domainmodel.SignalCategory
import com.schwarzit.lovenpark.signal.signalmodel.Picture
import com.schwarzit.lovenpark.signal.signalmodel.Signal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class SignalRootViewModel @Inject constructor(
    private val signalRepository: SignalRepository,
    private val keyStoreManager: KeyStoreManager,
    private val dialogProvider: DialogProvider,
    private val bitmapProcessor: BitmapProcessor,
    private val stringProvider: StringProvider
) : ViewModel() {
    var signalCategory: String? = null
    var location: LatLng? = null
    var locationTemp: LatLng? = null
    var photoURIs = mutableListOf<Uri>()
    var description: String? = null
    var date: String? = null
    var time: String? = null
    var year: String? = null
    var screenshot: Bitmap? = null

    val isUploadSignalInProgress = MutableLiveData<Boolean?>()
    var dialogMutableLiveData = MutableLiveData<SignalSubmissionDialog?>()

    var signal: Signal? = null

    val isCountDownTimerActive = MutableLiveData<Boolean>()

    private val _isSignalDescriptionInputCorrect = MutableLiveData<Boolean?>()
    val isSignalDescriptionInputCorrect = _isSignalDescriptionInputCorrect

    fun setIsSignalDescriptionCorrect(isCorrect: Boolean) {
        _isSignalDescriptionInputCorrect.postValue(isCorrect)
    }

    fun makeLocationEqualToTempLocation() {
        location = locationTemp
    }

    private val _isPhotoNumberHigherThanMin = MutableLiveData<Boolean?>()
    val isPhotoNumberHigherThanMin = _isPhotoNumberHigherThanMin

    fun setNextButtonState(isEnabled: Boolean) {
        _isPhotoNumberHigherThanMin.postValue(isEnabled)
    }

    fun postSignal(imageOutputDirectory: File) {
        val convertedScreenshot = convertScreenshot(imageOutputDirectory)
        signal?.pictures?.add(convertedScreenshot)
        dialogMutableLiveData.postValue(null)
        isUploadSignalInProgress.postValue(true)
        viewModelScope.launch {
            object : CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    isCountDownTimerActive.postValue(true)
                }
            }.start()
            try {
                signal?.let { signalRepository.postSignal(it) }
                isUploadSignalInProgress.postValue(false)
                isCountDownTimerActive.postValue(false)
                dialogMutableLiveData.postValue(
                    createDialog(
                        R.string.signal_creation_success_dialog_title,
                        R.string.signal_creation_success_dialog_description,
                        isCloseVisible = false,
                        isOkVisible = true,
                        isImageVisible = true
                    )
                )
                clearSignal()
            } catch (e: Exception) {
                isUploadSignalInProgress.postValue(false)
                isCountDownTimerActive.postValue(false)
                dialogMutableLiveData.postValue(
                    createDialog(
                        R.string.signal_creation_failure_dialog_title,
                        R.string.signal_creation_failure_dialog_message,
                        isCloseVisible = true,
                        isOkVisible = false
                    )
                )

            }
        }
    }

    private fun getImageUri( inImage: Bitmap, outputDirectory: File): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 60, bytes)
        val photoFile = File(
            outputDirectory, LP_SIGNAL + SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + FILE_EXTENSION

        )
        photoFile.createNewFile()
        val fo = FileOutputStream(photoFile)
        fo.write(bytes.toByteArray())
        fo.close()

        return photoFile.toUri()
    }


    private fun convertScreenshot(outputDirectory: File): Picture? {
        val screenshotUri = screenshot?.let { getImageUri(it, outputDirectory) }
        if (screenshotUri != null) {
            return createPicture(screenshotUri)
        }
        return null
    }

    fun createSignal(context: Context): Signal? {
        val categoryEnum = signalCategory?.let { mapCategoryToSignalCategoryEnum(it) }
        val description = description
        val latitude = location?.latitude
        val longitude = location?.longitude
        val pictureList = mapPhotoUrisToPictureList()
        val dayMonth = date?.split("/")
        val month = dayMonth?.get(1)
        val day = dayMonth?.get(0)
        val createdAt = stringProvider.getCreatedAt(
            R.string.date_format,
            UserSharedPrefHelper.getSignalYear(context),
            month,
            day,
            time
        )
        return if (categoryEnum != null && description != null && latitude != null && longitude != null) {
            Signal(
                categoryEnum, description, latitude, longitude,
                pictureList as MutableList<Picture?>, createdAt
            )
        } else {
            null
        }
    }

    fun clearSignal() {
        signalCategory = null
        location = null
        locationTemp = null
        photoURIs.apply {
            forEach { uri -> bitmapProcessor.deleteFile(uri) }
            clear()
        }
        description = null
        date = null
        time = null
        year = null
        isPhotoNumberHigherThanMin.postValue(null)
    }

    fun isSignalDataCorrect() = photoURIs.isNotEmpty()
            && location?.isUserLocationInPark() == true
            && signalCategory?.isNotEmpty() == true
            && description?.isNotEmpty() == true
            && date?.isNotEmpty() == true

    private fun mapPhotoUrisToPictureList() = photoURIs.map { createPicture(it) }

    private fun createPicture(uri: Uri): Picture? {
        val array = uri.toString().split("/")
        val name = array[array.size - 1]
        val resized =
            bitmapProcessor.processBitmap(uri, REQUIRED_SIZE, false)
        val content = resized?.let { bitmapProcessor.convertBitmapToByteArray(it) }
        val pictureToBase64 = content?.toBase64()
        val mimeType = bitmapProcessor.getMimeType(uri)
        return content?.let { Picture(name, pictureToBase64, mimeType) }
    }

    private fun ByteArray.toBase64(): String =
        String(Base64.getEncoder().encode(this))

    private fun createDialog(
        title: Int,
        description: Int,
        isCloseVisible: Boolean,
        isOkVisible: Boolean,
        isImageVisible: Boolean = false
    ): SignalSubmissionDialog = dialogProvider.getSignalSubmissionDialog(
        title,
        description,
        isCloseVisible,
        isOkVisible,
        isImageVisible = isImageVisible
    )

    private fun mapCategoryToSignalCategoryEnum(category: String): SignalCategory {
        return when (category) {
            stringProvider.getCategory(R.string.signal_category_injured_tree) -> {
                SignalCategory.INJURED_TREE
            }

            stringProvider.getCategory(R.string.signal_category_damaged_park_furniture) -> {
                SignalCategory.DAMAGED_PARK_FURNITURE
            }

            stringProvider.getCategory(R.string.signal_category_uncollected_waste) -> {
                SignalCategory.UNCOLLECTED_WASTE
            }

            stringProvider.getCategory(R.string.signal_category_animal_carcasses_found) -> {
                SignalCategory.ANIMAL_CARCASSES_FOUND
            }

            else -> {
                SignalCategory.OTHER
            }
        }
    }

}