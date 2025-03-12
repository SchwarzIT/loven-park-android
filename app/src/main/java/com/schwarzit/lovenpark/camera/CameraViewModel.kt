package com.schwarzit.lovenpark.camera

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.schwarzit.lovenpark.signal.PHOTOS_MAX_NUMBER
import com.schwarzit.lovenpark.signal.PHOTOS_MIN_NUMBER
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {
    var cameraDenied = false
    var galleryDenied = false
    var listUris = mutableListOf<Uri>()

    private val _imageNumberEqualOrHigherThanMinNumberMutableLiveData = MutableLiveData<Boolean>()
    val imageNumberEqualOrHigherThanMinNumberLiveData: LiveData<Boolean> =
        _imageNumberEqualOrHigherThanMinNumberMutableLiveData

    private val _imageNumberEqualOrHigherThanMaxNumberMutableLiveData = MutableLiveData<Boolean>()
    val imageNumberEqualOrHigherThanMaxNumberLiveData: LiveData<Boolean> =
        _imageNumberEqualOrHigherThanMaxNumberMutableLiveData

    fun addImage(uri: Uri) {
        if (listUris.size < PHOTOS_MAX_NUMBER) {
            listUris.add(uri)
        }
    }

    fun removeImage(position: Int) {
        if (position >= 0 && position < listUris.size) {
            listUris.removeAt(position)
        }
    }

    fun checkPhotoMinNumber() {
        val imageNumberEqualOrHigherThanMinNumber = listUris.size >= PHOTOS_MIN_NUMBER
        _imageNumberEqualOrHigherThanMinNumberMutableLiveData.postValue(
            imageNumberEqualOrHigherThanMinNumber
        )
    }

    fun checkIfPhotoNumberReachedMax() {
        val imageNumberEqualOrHigherThanMaxNumber = listUris.size >= PHOTOS_MAX_NUMBER
        _imageNumberEqualOrHigherThanMaxNumberMutableLiveData.postValue(
            imageNumberEqualOrHigherThanMaxNumber
        )
    }

}