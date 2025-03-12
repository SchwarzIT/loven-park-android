package com.schwarzit.lovenpark

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.schwarzit.lovenpark.profile.data.UserRepository
import com.schwarzit.lovenpark.data.utils.UserSharedPrefHelper
import com.schwarzit.lovenpark.registration.model.Email
import kotlinx.coroutines.launch

class LoginScreenViewModel(apl: Application) : AndroidViewModel(apl) {

    private val _buttonStateMutableLiveData = MutableLiveData<Boolean>()
    val buttonStateLiveData: LiveData<Boolean> = _buttonStateMutableLiveData

    private val _emailValidationMutableLiveData = MutableLiveData<Boolean>()
    val emailValidationLiveData: LiveData<Boolean> = _emailValidationMutableLiveData

    private val _loginSuccessMutableLiveData = MutableLiveData<Boolean>()
    val loginSuccessLiveData: LiveData<Boolean> = _loginSuccessMutableLiveData

    private val _passwordMutableLiveData = MutableLiveData<Boolean>()
    val passwordLiveData: LiveData<Boolean> = _passwordMutableLiveData

    fun onInputDataChanged(email: String, password: String) {
        _buttonStateMutableLiveData.postValue(password.isNotEmpty() && isEmailValid(email))
        _passwordMutableLiveData.postValue(password.isEmpty())
    }

    fun onInputEmailChanged(email: String) {
        _emailValidationMutableLiveData.postValue(isEmailValid(email))
    }

    fun isEmailValid(email: String): Boolean {
        if (!Email(email).isDataEmpty()) {
            return Email(email).isDataValid()
        }
        return false
    }

    fun loginUser(context: Context, email: String, password: String) {
        viewModelScope.launch {
            _loginSuccessMutableLiveData
                .postValue((UserRepository.login(context, email, password) != null))
        }
    }

    fun isUserLogged(context: Context) =
        (UserSharedPrefHelper.getLoggedUserEmail(context)?.isNotEmpty() == true)

}