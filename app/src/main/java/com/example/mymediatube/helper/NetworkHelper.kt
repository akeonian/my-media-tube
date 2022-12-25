package com.example.mymediatube.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NetworkHelper {

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected

    private val networkCallback: Any = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        object: NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                _isConnected.postValue(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                _isConnected.postValue(false)
            }
        }
    } else Unit

    fun registerCallback(context: Context) {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()
            connectivityManager.registerNetworkCallback(
                networkRequest, networkCallback as NetworkCallback)
        }
    }

    fun unregisterCallback(context: Context) {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(networkCallback as NetworkCallback)
        }
    }

    // checks connection and calls callback for api < lollipop
    fun checkConnection(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val connection = (context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo?.isConnected ?: false
            if (connection != isConnected.value) _isConnected.postValue(connection)
            connection
        } else isConnected.value ?: false
    }
}