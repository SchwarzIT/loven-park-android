package com.schwarzit.lovenpark.network

import com.schwarzit.lovenpark.mappin.data.remote.MapPinModel
import com.schwarzit.lovenpark.profile.data.remote.UpdateUserInfo
import com.schwarzit.lovenpark.profile.data.remote.UserRemoteModel
import com.schwarzit.lovenpark.profile.signals.data.remote.SignalRemoteModel
import com.schwarzit.lovenpark.signal.signalmodel.Signal
import com.schwarzit.lovenpark.signal.signalmodel.response.SignalResponse
import com.schwarzit.lovenpark.user.TokenInfo
import retrofit2.Response
import retrofit2.http.*

interface LovenParkService {

    @GET("api/v1/poi")
    suspend fun getAllPointsOfInterest(
        @Query("language") language: String
    ): Response<List<MapPinModel>>

    @POST("api/v1/login")
    suspend fun login(): TokenInfo

    @GET("api/v1/signals")
    suspend fun getActiveSignals(): Response<List<SignalRemoteModel>>

    @GET("api/v1/signals/getAllSignalsForUser")
    suspend fun getUserSignals(): Response<List<SignalRemoteModel>>

    @GET("api/v1/signals/getAllSignalsFromOthers")
    suspend fun getOthersSignals(
    ): Response<List<SignalRemoteModel>>

    @POST("api/v1/signals/create")
    suspend fun postNewSignal(
        @Body signal: Signal
    ): SignalResponse

    @GET("api/v1/users/getUser")
    suspend fun getUser(): Response<UserRemoteModel>

    @PATCH("api/v1/users/update")
    suspend fun updateUser(
        @Body user: UpdateUserInfo): Response<UpdateUserInfo>

    @DELETE("api/v1/users/delete")
    suspend fun deleteUser(): Response<Unit>

}

