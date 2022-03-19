package com.learnpa

import android.content.Context
import android.content.pm.PackageManager
import com.learnpa.app.MyApplication

object Metar {
    private var app: MyApplication? = null
    operator fun get(key: String): String {
        return try {
            val ai = (app as Context)
                .packageManager
                .getApplicationInfo(
                    (app as Context)
                        .packageName,
                    PackageManager.GET_META_DATA
                )
            val bundle = ai.metaData
            bundle.getString(key)?:""
        } catch (e: PackageManager.NameNotFoundException) {
            ""
        }
    }
    fun initialize(app: MyApplication) {
        Metar.app = app
    }
}