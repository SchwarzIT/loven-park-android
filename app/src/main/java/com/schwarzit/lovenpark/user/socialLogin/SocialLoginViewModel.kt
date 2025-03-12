package com.schwarzit.lovenpark.user.socialLogin

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarzit.lovenpark.keyStore.KeyStoreManager
import com.schwarzit.lovenpark.keyStore.KeyStoreSharedPrefsKeys
import com.schwarzit.lovenpark.profile.signals.data.domainmodel.SignalModel
import com.schwarzit.lovenpark.user.google.GoogleSignInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialLoginViewModel @Inject constructor(
    private val repository: SocialLoginRepository,
    val keyStoreManager: KeyStoreManager
) :
    ViewModel() {
    private val _liveDataLoggedUser = MutableLiveData<Boolean>()
    val liveDataLoggedUser: LiveData<Boolean> = _liveDataLoggedUser

    fun googleLogin(context: Context) {
        viewModelScope.launch {
            try {
                val result = repository.loginUser()

                result.accessToken.let {
                    keyStoreManager.encryptToken(
                        it,
                        KeyStoreSharedPrefsKeys.ACCESS_TOKEN_PAIR_FIRST_KEY,
                        KeyStoreSharedPrefsKeys.ACCESS_TOKEN_PAIR_SECOND_KEY
                    )
                }

                result.refreshToken.let {
                    keyStoreManager.encryptToken(
                        it,
                        KeyStoreSharedPrefsKeys.REFRESH_TOKEN_PAIR_FIRST_KEY,
                        KeyStoreSharedPrefsKeys.REFRESH_TOKEN_PAIR_SECOND_KEY
                    )
                }

                SocialUserSharedPrefs.saveLoggedUser(context,
                    SocialLoginSharedPrefKeys.USER_LOGGED_KEY
                )

                _liveDataLoggedUser.postValue(true)

            } catch (e: Exception) {
                _liveDataLoggedUser.postValue(false)
                Log.e("request_", "${e.message}")
            }
        }
    }
}