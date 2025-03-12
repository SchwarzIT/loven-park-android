package com.schwarzit.lovenpark.profile.signals.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarzit.lovenpark.keyStore.KeyStoreManager
import com.schwarzit.lovenpark.profile.signals.data.SignalsRepository
import com.schwarzit.lovenpark.profile.signals.data.domainmodel.SignalModel
import com.schwarzit.lovenpark.profile.signals.data.remote.toSignalModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.internal.IOException
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SignalsViewModel @Inject constructor(
    var application: Application,
    private val signalsRepository: SignalsRepository,
    val keyStoreManager: KeyStoreManager
) :
    ViewModel() {

    private val _liveDataAllActiveSignals = MutableLiveData<List<SignalModel>>()
    val liveDataAllActiveSignals: LiveData<List<SignalModel>> = _liveDataAllActiveSignals

    private val _liveDataOthersSignals = MutableLiveData<List<SignalModel>>()
    val liveDataOthersSignals: LiveData<List<SignalModel>> = _liveDataOthersSignals

    private val _liveDataUserSignals = MutableLiveData<List<SignalModel>>()
    val liveDataUserSignals: LiveData<List<SignalModel>> = _liveDataUserSignals

    private val _liveDataSignal = MutableLiveData<SignalModel?>()
    val liveDataSignal: MutableLiveData<SignalModel?> = _liveDataSignal

    private val _isLoadSignalsInProgress = MutableLiveData(false)
    var isLoadSignalsInProgress: LiveData<Boolean> = _isLoadSignalsInProgress

    fun requestActiveSignalsWhenOnline() {
        viewModelScope.launch {
            try {
                val result = signalsRepository.getActiveSignals()
                _liveDataAllActiveSignals.postValue(result)
            } catch (e: HttpException) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
            } catch (e: Exception) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
                e.printStackTrace()
            }
        }
    }

    fun requestActiveSignalsWhenOffline() {
        viewModelScope.launch {
            try {
                val result = signalsRepository.getSavedActiveSignalsFromDatabase()
                _liveDataAllActiveSignals.postValue(result)
            } catch (e: IOException) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
            } catch (e: Exception) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
                e.printStackTrace()
            }
        }
    }

    fun requestUserSignalsWhenOnline() {
        viewModelScope.launch {
            _isLoadSignalsInProgress.postValue(true)

            try {
                val result =
                    signalsRepository.getUserSignals()
                _liveDataUserSignals.postValue(result)
            } catch (e: HttpException) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
            } catch (e: Exception) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
                e.printStackTrace()
            }

            _isLoadSignalsInProgress.postValue(false)
        }
    }

    fun requestUserSignalsWhenOffline(context: Context) {
        viewModelScope.launch {
            _isLoadSignalsInProgress.postValue(true)

            try {
                val result = signalsRepository.getSavedUserSignalsFromDatabase(context)
                _liveDataUserSignals.postValue(result)
            } catch (e: IOException) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
            } catch (e: Exception) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
                e.printStackTrace()
            }

            _isLoadSignalsInProgress.postValue(false)
        }
    }

    fun requestLatestUserSignals() {
        viewModelScope.launch {
            try {
                val result =
                    signalsRepository.getRemoteUserSignals()
                        .map {
                            it.toSignalModel()
                        }
                _liveDataUserSignals.postValue(result)

            } catch (e: HttpException) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
            } catch (e: Exception) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
                e.printStackTrace()
            }
        }
    }

    fun requestOthersSignalsWhenOnline() {
        viewModelScope.launch {
            _isLoadSignalsInProgress.postValue(true)

            try {
                val result =
                    signalsRepository.getOthersSignals()
                _liveDataOthersSignals.postValue(result)

            } catch (e: HttpException) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
            } catch (e: Exception) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
                e.printStackTrace()
            }

            _isLoadSignalsInProgress.postValue(false)
        }
    }

    fun requestOthersSignalsWhenOffline(context: Context) {
        viewModelScope.launch {
            _isLoadSignalsInProgress.postValue(true)
            try {
                val result = signalsRepository.getSavedOthersSignalsFromDatabase(context)
                _liveDataOthersSignals.postValue(result)
            } catch (e: IOException) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
            } catch (e: Exception) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
                e.printStackTrace()
            }
            _isLoadSignalsInProgress.postValue(false)
        }
    }

    fun requestLatestOthersSignals() {
        viewModelScope.launch {
            try {
                val result =
                    signalsRepository.getRemoteOthersSignals()
                        .map {
                            it.toSignalModel()
                        }
                _liveDataUserSignals.postValue(result)

            } catch (e: HttpException) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
            } catch (e: Exception) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
                e.printStackTrace()
            }
        }
    }

    fun requestSignalWhenOnline(signalId: String) {
        viewModelScope.launch {
            try {
                val result = signalsRepository.getRemoteSignal(signalId)
                _liveDataSignal.postValue(result)

            } catch (e: HttpException) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
            } catch (e: Exception) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
                e.printStackTrace()
            }
        }
    }

    fun requestSignalWhenOffline(signalId: String) {
        viewModelScope.launch {
            try {
                val result = signalsRepository.getSavedSignalFromDatabase(signalId)
                _liveDataSignal.postValue(result)
            } catch (e: IOException) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
            } catch (e: Exception) {
                e.message?.let { Log.e(SignalsRepository::class.java.simpleName, it) }
                e.printStackTrace()
            }
        }
    }
}
