package com.learnpa.repository_impl

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.learnpa.Resource
import com.learnpa.repository.DataStore
import kotlinx.coroutines.flow.*
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("myPreference")

class DataStoreImpl @Inject constructor(
    private val context: Context
    ): DataStore {

    override fun getBaseUrlFlow(): Flow<String> =
        context.dataStore.data
        .map { preferences ->
            preferences[BASE_URL] as String
        }

    override suspend fun getBaseUrl() =  context.dataStore.data.first()[BASE_URL]?:""

    override suspend fun setBaseUrl(baseUrl: String) {
        context.dataStore.edit { preferences ->
            preferences[BASE_URL] = baseUrl
        }
    }
    companion object {
        val BASE_URL = stringPreferencesKey("base_url")
    }
}