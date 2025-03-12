package com.schwarzit.lovenpark.profile.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarzit.lovenpark.profile.data.UserProfileRepository
import com.schwarzit.lovenpark.profile.data.domainmodel.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val repositoryUser: UserProfileRepository
) :
    ViewModel() {
    val liveDataUser = MutableLiveData<UserModel?>()

    fun getUserWhenOnline() {
        try {
            viewModelScope.launch {
                val result = repositoryUser.getUser()
                liveDataUser.postValue(result)
            }
        } catch (e: Exception) {
            Log.e("request_", "${e.message}")
        }
    }

    fun requestUserWhenOffline(context: Context) {
        viewModelScope.launch {
            val result = repositoryUser.getSavedUserFromDatabase(context)
            liveDataUser.postValue(result)
        }
    }
}
