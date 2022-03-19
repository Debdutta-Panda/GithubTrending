package com.learnpa.repository

import com.learnpa.ConnectivityListener

interface NetStat {
    val current: ConnectivityListener.Net
    suspend fun waitForOnline(): Boolean
}