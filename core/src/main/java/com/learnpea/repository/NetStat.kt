package com.learnpea.repository

import com.learnpea.models.Net


interface NetStat {
    val current: Net
    suspend fun waitForOnline(): Boolean
}