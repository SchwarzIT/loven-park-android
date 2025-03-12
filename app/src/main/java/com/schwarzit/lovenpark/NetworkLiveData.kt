package com.schwarzit.lovenpark

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData

class NetworkLiveData(application: Context) : LiveData<Boolean>() {

    /*
    Sets default offline mode
     */
    init {
        postValue(false)
    }

    private val connectivityManager =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private fun networkCallback() = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)

        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }

    }

    override fun onActive() {
        super.onActive()
        val networkRequest = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(networkRequest.build(), networkCallback())
    }

    override fun onInactive() {
        super.onInactive()
        try {
            connectivityManager.unregisterNetworkCallback(networkCallback())
        } catch (e: Exception) {
            Log.d("NETWORK_EXCEPTION", "${e.message}")
        }
    }

}
