package com.learnpa

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import com.learnpea.models.Net

class ConnectivityListener(private val context: Context) {



    val net = MutableLiveData<Net>()

    //region used on api < 24
    private val intentFilter by lazy {
        IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    }

    private val networkBroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val _net = isConnected()
                net.postValue(_net)
            }
        }
    }
    //endregion

    private val networkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val isMetered = cm.isActiveNetworkMetered

                net.postValue(Net(true,isMetered))
            }

            override fun onLost(network: Network) {
                net.postValue(Net(false, metered = true))
            }
        }
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getConnectivityManager()?.registerDefaultNetworkCallback(networkCallback)
        } else {
            context.registerReceiver(networkBroadcastReceiver, intentFilter)
        }
    }

    fun isConnected(): Net {
        val activeNetwork = getConnectivityManager()?.activeNetworkInfo
        val isConnected = activeNetwork?.isConnectedOrConnecting == true

        if(isConnected)
        {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val isMetered = cm.isActiveNetworkMetered

            return Net(isConnected,isMetered)
        }
        else
        {
            return Net(false, metered = true)
        }
    }

    fun getConnectivityManager() = getSystemService(context, ConnectivityManager::class.java)
}