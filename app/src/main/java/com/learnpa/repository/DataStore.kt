package com.learnpa.repository

import kotlinx.coroutines.flow.Flow

interface DataStore {
    suspend fun setBaseUrl(url: String)
    fun getBaseUrlFlow(): Flow<String>
    suspend fun getBaseUrl(): String
}