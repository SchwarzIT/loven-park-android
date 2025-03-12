package com.schwarzit.lovenpark.profile.signals.data.remote

import com.schwarzit.lovenpark.network.LovenParkService
import javax.inject.Inject

class SignalsRemoteDataSource @Inject constructor(private val apiClient: LovenParkService) {

    suspend fun getAllActiveSignals(): List<SignalRemoteModel> =
        apiClient.getActiveSignals().body() ?: emptyList()

    suspend fun getAllUserSignals(): List<SignalRemoteModel> =
        apiClient.getUserSignals().body()?: emptyList()

    suspend fun getAllOthersSignals(): List<SignalRemoteModel> =
        apiClient.getOthersSignals().body() ?: emptyList()
}