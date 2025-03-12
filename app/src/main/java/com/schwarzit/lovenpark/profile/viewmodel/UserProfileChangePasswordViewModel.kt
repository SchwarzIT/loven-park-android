package com.schwarzit.lovenpark.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.schwarzit.lovenpark.registration.model.Password

class UserProfileChangePasswordViewModel : ViewModel() {

    private val _buttonStateMutableLiveData = MutableLiveData<Boolean>()
    val buttonStateLiveData: LiveData<Boolean> = _buttonStateMutableLiveData

    fun onInputDataChanged(oldPassword: String, newPassword: String) {
        _buttonStateMutableLiveData.value =
            isNewPasswordValid(newPassword) && oldPassword.isNotEmpty()
    }

    fun isNewPasswordValid(newPassword: String): Boolean {
        if (newPassword.isNotEmpty()) {
            return Password(newPassword).isDataValid()
        }
        return false
    }

}