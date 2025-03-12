package com.schwarzit.lovenpark

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.schwarzit.lovenpark.profile.data.UserRepository
import com.schwarzit.lovenpark.data.utils.UserSharedPrefHelper
import com.schwarzit.lovenpark.registration.model.*
import kotlinx.coroutines.launch

class RegisterScreenViewModel(apl: Application) : AndroidViewModel(apl) {

    private val _registerButtonStateMutableLiveData = MutableLiveData<Boolean>()
    val registerButtonStateLiveData: LiveData<Boolean> = _registerButtonStateMutableLiveData

    private val _registerSuccessMutableLiveData = MutableLiveData<Boolean>()
    val registerSuccessLiveData: LiveData<Boolean> = _registerSuccessMutableLiveData

    fun onInputDataChanged(
        name: String,
        email: String,
        phoneNumber: String,
        password: String,
        checkBoxState: Boolean
    ) {
        _registerButtonStateMutableLiveData.postValue(
            isAllInputValid(
                name,
                email,
                phoneNumber,
                password,
                checkBoxState
            )
        )
    }

    fun isAllInputValid(
        name: String,
        email: String,
        phoneNumber: String,
        password: String,
        checkBoxState: Boolean
    ): Boolean = isNameValid(name) == true
            && isEmailValid(email) == true
            && isPhoneNumberValid(phoneNumber) == true
            && isPasswordValid(password) == true
            && checkBoxState

    fun isNameValid(name: String): Boolean? {
        if (Name(name).isDataEmpty().not()) {
            return Name(name).isDataValid()
        }
        return null
    }

    fun isEmailValid(email: String): Boolean? {
        if (!Email(email).isDataEmpty()) {
            return Email(email).isDataValid()
        }
        return null
    }

    fun isPhoneNumberValid(phoneNumber: String): Boolean? {
        if (!PhoneNumber(phoneNumber).isDataEmpty()) {
            return PhoneNumber(phoneNumber).isDataValid()
        }
        return null
    }

    fun isPasswordValid(password: String): Boolean? {
        if (!Password(password).isDataEmpty()) {
            return Password(password).isDataValid()
        }
        return null
    }

    fun registerUser(context: Context, user: User) {
        viewModelScope.launch {
            _registerSuccessMutableLiveData
                .postValue(
                    (UserRepository.registerUser(context, user) != null))
        }
    }

    fun isUserRegistered(context: Context) =
        (UserSharedPrefHelper.getLoggedUserEmail(context)?.isNotEmpty() == true)

}