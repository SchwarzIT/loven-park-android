package com.schwarzit.lovenpark.network

import android.content.Context
import com.schwarzit.lovenpark.CoreConfig
import com.schwarzit.lovenpark.keyStore.KeyStoreManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LovenParkApiClient {
    fun getClient(context: Context, refreshTokenApiClient: RefreshTokenService, keyStoreManager: KeyStoreManager): LovenParkService {

        val interceptor = LovenParkInterceptor(context, refreshTokenApiClient, keyStoreManager)
        val baseUrl = CoreConfig().lovenParkBaseUrl

        val httpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .addInterceptor(interceptor)
        val retrofit = Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .client(httpClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(LovenParkService::class.java)
    }
}