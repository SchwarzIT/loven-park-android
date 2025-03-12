package com.schwarzit.lovenpark.signal

import com.schwarzit.lovenpark.signal.signalmodel.Signal
import com.schwarzit.lovenpark.utils.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignalRepository @Inject constructor(private val remoteDataSource: SignalRemoteData) {
    suspend fun postSignal(signal: Signal) {
        withContext(Dispatchers.IO) {
            remoteDataSource.postSignal(signal)
        }
    }
}