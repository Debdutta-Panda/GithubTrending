package com.learnpa.app

import android.app.Application
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.learnpa.ConnectivityListener
import com.learnpa.R
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        instance = this

        ConnectivityListener(this).net.observeForever {
            _connectivity.postValue(it)
        }
    }


    companion object {
        var net: ConnectivityListener.Net = ConnectivityListener.Net()
        var instance: MyApplication? = null
        private val _connectivity = MutableLiveData<ConnectivityListener.Net>()
        val connectivity: LiveData<ConnectivityListener.Net> = _connectivity
        init {
            connectivity.observeForever { result->
                net.on = result.on
                net.metered = result.metered
                connectivityListeners.forEach { listener->
                    listener(result)
                }
            }
        }
        private val connectivityListeners = mutableListOf<(ConnectivityListener.Net)->Unit>()
        fun registerConnectivityListener(listener: (ConnectivityListener.Net)->Unit){
            connectivityListeners.add(listener)
        }
        fun unRegisterConnectivityListener(listener: (ConnectivityListener.Net)->Unit){
            connectivityListeners.remove(listener)
        }
    }
    fun stringResource(@StringRes stringId: Int):String
    {
        return try {
            resources.getString(stringId)
        } catch (e: Exception) {
            resources.getString(R.string.error_string)
        }
    }

    fun drawableResource(@DrawableRes drawableId: Int):Drawable?
    {
        return try {
            ContextCompat.getDrawable(this,drawableId)
        } catch (e: Exception) {
            null
        }
    }

    fun dimensionResource(@DimenRes dimenRes: Int):Float?
    {
        return try {
            resources.getDimension(dimenRes)
        } catch (e: Exception) {
            0f
        }
    }

    fun colorResource(@ColorRes colorId: Int):Int
    {
        return try {
            ContextCompat.getColor(this,colorId)
        } catch (e: Exception) {
            Color.TRANSPARENT
        }
    }
}