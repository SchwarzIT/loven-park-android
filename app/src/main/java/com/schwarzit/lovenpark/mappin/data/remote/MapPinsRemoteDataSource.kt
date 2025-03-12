package com.schwarzit.lovenpark.mappin.data.remote

import com.schwarzit.lovenpark.network.LovenParkService
import retrofit2.Response
import javax.inject.Inject

class MapPinsRemoteDataSource @Inject constructor(private val apiClientLoven: LovenParkService) {

    /**
     * Requests new pins from the server
     */
    suspend fun getAllPointsOfInterest(language: String): Response<List<MapPinModel>> =
        apiClientLoven.getAllPointsOfInterest(language)

}

