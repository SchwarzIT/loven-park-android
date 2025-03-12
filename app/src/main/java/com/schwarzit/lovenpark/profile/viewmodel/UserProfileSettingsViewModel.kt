package com.schwarzit.lovenpark.profile.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.schwarzit.lovenpark.data.utils.UserSharedPrefHelper
import com.schwarzit.lovenpark.profile.data.UserProfileRepository
import com.schwarzit.lovenpark.profile.data.UserRepository
import com.schwarzit.lovenpark.profile.data.remote.UpdateUserInfo
import com.schwarzit.lovenpark.profile.data.remote.UserRemoteModel
import com.schwarzit.lovenpark.registration.model.*
import com.schwarzit.lovenpark.user.google.GoogleSignInRepository
import com.schwarzit.lovenpark.user.socialLogin.SocialUserSharedPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileSettingsViewModel @Inject constructor(
    private val repository: UserProfileRepository,
    application: Application
) :
    AndroidViewModel(application) {

    private var _loggedUserMutableLiveData: MutableLiveData<UserRemoteModel> =
        MutableLiveData<UserRemoteModel>()
    val loggedUserLiveData: LiveData<UserRemoteModel> = _loggedUserMutableLiveData

    private var _loggedUserRealmMutableLiveData: MutableLiveData<User> =
        MutableLiveData<User>()
    val loggedUserRealmLiveData: LiveData<User> = _loggedUserRealmMutableLiveData

    private val _saveChangesButtonStateMutableLiveData = MutableLiveData<Boolean>()
    val saveChangesButtonStateLiveData: LiveData<Boolean> = _saveChangesButtonStateMutableLiveData

    private var _liveDataChangedUser: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val liveDataChangedUser: LiveData<Boolean> = _liveDataChangedUser

    private var _liveDataDeletedUser: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val liveDataDeletedUser: LiveData<Boolean> = _liveDataDeletedUser

    var email: String? = null

    fun requestLoggedUserRealm(context: Context) {
        viewModelScope.launch {
            val email = Email(UserSharedPrefHelper.getLoggedUserEmail(context).toString())
            val user = UserRepository.getUser(email)
            val newUser = User(
                Name(user?.name?.name.toString()),
                Email(user?.email?.email.toString()),
                PhoneNumber(user?.phoneNumber?.phoneNumber.toString()),
                Password(user?.password?.password.toString())
            )

            _loggedUserRealmMutableLiveData.value = newUser
        }

    }

    fun requestLoggedUser() {
        viewModelScope.launch {
            try {
                val result = repository.getUserRemote()
                email = result.email

                val newUser = UserRemoteModel(
                    result.id,
                    result.fullName,
                    result.email,
                    result.phoneNumber,
                    result.profilePicture,
                    result.badge
                )

                _loggedUserMutableLiveData.value = newUser
            } catch (e: Exception) {
                Log.e("request_", "${e.message}")
            }
        }
    }

    fun requestUpdateUser(email: String, fullName: String, phoneNumber: String) {
        val user = UpdateUserInfo(
            email,
            fullName,
            phoneNumber
        )
        try {
            viewModelScope.launch {
                repository.updateUser(user)
                _liveDataChangedUser.postValue(true)

            }
        } catch (e: Exception) {
            SocialUserSharedPrefs.removeUser(getApplication<Application>().applicationContext)
            GoogleSignInRepository(getApplication<Application>().applicationContext).signOut()
            Log.e("request_", "${e.message}")
        }

    }

    fun deleteProfile() {
        try {
            viewModelScope.launch {
                val response = repository.deleteProfile()
                _liveDataDeletedUser.postValue(response.isSuccessful)
            }
        } catch (e: Exception) {
            Log.e("request_", "${e.message}")
        }
    }

    fun onInputDataChanged(
        name: String,
        phoneNumber: String = "",
    ) {
        _saveChangesButtonStateMutableLiveData.postValue(
            isAllInputValid(
                name,
                phoneNumber,
            )
        )
    }

    fun isInputDataChanged(
        name: String,
        phoneNumber: String
    ) = _loggedUserMutableLiveData.value?.fullName != name
            || _loggedUserMutableLiveData.value?.phoneNumber != phoneNumber


    fun isAllInputValid(
        name: String,
        phoneNumber: String = "",
    ) = isNameValid(name) == true
            && (isPhoneNumberValid(phoneNumber) == true || isPhoneNumberValid(phoneNumber) == null)

    fun isNameValid(name: String): Boolean? {
        if (Name(name).isDataEmpty().not()) {
            return Name(name).isDataValid()
        }
        return null
    }

    fun isPhoneNumberValid(phoneNumber: String): Boolean? {
        if (!PhoneNumber(phoneNumber).isDataEmpty()) {
            return PhoneNumber(phoneNumber).isDataValid()
        }
        return null
    }

}