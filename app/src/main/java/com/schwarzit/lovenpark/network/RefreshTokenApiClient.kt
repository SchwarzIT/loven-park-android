package com.schwarzit.lovenpark.network

import com.schwarzit.lovenpark.CoreConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RefreshTokenApiClient {
    private val interceptor = HttpLoggingInterceptor().apply { level =  HttpLoggingInterceptor.Level.BODY }
    private val httpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder().addInterceptor(interceptor)
    private val baseUrl = CoreConfig().lovenParkBaseUrl

    private val retrofit = Retrofit.Builder()
        .client(httpClientBuilder.build())
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getClient():RefreshTokenService{
        return retrofit.create(RefreshTokenService::class.java)
    }
}