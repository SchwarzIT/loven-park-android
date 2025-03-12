package com.schwarzit.lovenpark.signal

import com.schwarzit.lovenpark.network.LovenParkService
import com.schwarzit.lovenpark.signal.signalmodel.Signal
import com.schwarzit.lovenpark.signal.signalmodel.response.SignalResponse
import javax.inject.Inject

class SignalRemoteData @Inject constructor(private val apiClientLoven: LovenParkService) {

    suspend fun postSignal(signal: Signal): SignalResponse = apiClientLoven.postNewSignal(signal)
}