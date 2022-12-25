package com.example.mymediatube.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mymediatube.helper.NetworkHelper

class ConnectionViewModel: ViewModel() {

    private val networkHelper = NetworkHelper()
    val isConnectedData: LiveData<Boolean> = networkHelper.isConnected
    val isConnected get() = isConnectedData.value ?: false

    fun checkConnection(context: Context) = networkHelper.checkConnection(context)


    fun startObservingConnection(context: Context) = networkHelper.registerCallback(context)


    fun stopObservingConnection(context: Context) = networkHelper.unregisterCallback(context)

}